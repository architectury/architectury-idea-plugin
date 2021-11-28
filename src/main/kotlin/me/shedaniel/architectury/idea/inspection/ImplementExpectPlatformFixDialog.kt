package me.shedaniel.architectury.idea.inspection

import com.intellij.ide.util.DirectoryChooser
import com.intellij.openapi.command.CommandProcessor
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.psi.JavaDirectoryService
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiMethod
import com.intellij.refactoring.RefactoringBundle
import com.intellij.refactoring.move.moveClassesOrPackages.DestinationFolderComboBox
import com.intellij.ui.EditorComboBox
import com.intellij.ui.components.JBLabelDecorator
import com.intellij.util.ui.FormBuilder
import com.intellij.util.ui.UIUtil
import me.shedaniel.architectury.idea.util.ArchitecturyBundle
import me.shedaniel.architectury.idea.util.Platform
import javax.swing.JComponent
import javax.swing.JLabel

class ImplementExpectPlatformFixDialog(
    private val project: Project,
    private val platform: Platform,
    private val packageName: String,
    private val method: PsiMethod,
    defaultDirectory: PsiDirectory?,
) : DialogWrapper(project, true) {
    private val destinationBox: DestinationFolderComboBox = object : DestinationFolderComboBox() {
        override fun getTargetPackage(): String {
            return packageName
        }

        override fun reportBaseInTestSelectionInSource(): Boolean {
            return true
        }
    }

    init {
        title = ArchitecturyBundle["inspection.implementExpectPlatform.single", platform]
        init()
        destinationBox.setData(project, defaultDirectory, EditorComboBox(""))
        destinationBox.comboBox.addItemListener {
            isOKActionEnabled = (it.item as? DirectoryChooser.ItemWrapper)?.directory != null
        }
    }

    override fun createActions() = arrayOf(okAction, cancelAction)

    override fun getPreferredFocusedComponent() = destinationBox.childComponent

    override fun createCenterPanel(): JComponent? = null

    override fun createNorthPanel(): JComponent {
        val label = JLabel(RefactoringBundle.message("target.destination.folder"))
        label.labelFor = destinationBox
        return FormBuilder.createFormBuilder()
            .addComponent(JBLabelDecorator.createJBLabelDecorator().setBold(true).apply {
                text = title
            })
            .addLabeledComponent(label, destinationBox, UIUtil.LARGE_VGAP)
            .panel
    }

    override fun doOKAction() {
        if (!okAction.isEnabled) {
            return
        }

        close(OK_EXIT_CODE, /* isOk = */ true)
        CommandProcessor.getInstance().executeCommand(project, {
            val direction = resolveFile(
                project,
                (destinationBox.comboBox.selectedItem as DirectoryChooser.ItemWrapper).directory,
                packageName,
            )

            val className = platform.getImplementationName(method.containingClass!!)
            val clazz = JavaDirectoryService.getInstance().getClasses(direction)
                .firstOrNull { it.name == className.substringAfterLast('.') }
                ?: JavaDirectoryService.getInstance().createClass(direction, className.substringAfterLast('.'))
            ImplementExpectPlatformFix.addMethod(project, method, clazz)
        }, title, null)
    }

    companion object {
        fun resolveFile(project: Project, selection: PsiDirectory, packageName: String): PsiDirectory {
            fun isParent(directory: PsiDirectory, parentCandidate: PsiDirectory): Boolean {
                var dir: PsiDirectory? = directory
                while (dir != null) {
                    if (dir == parentCandidate) return true
                    dir = dir.parentDirectory
                }
                return false
            }

            var file: PsiDirectory = selection
            val manager = PsiManager.getInstance(project)
            val sourceRoots = ProjectRootManager.getInstance(project).contentSourceRoots
            for (sourceRoot in sourceRoots) {
                if (sourceRoot.isDirectory) {
                    val directory = manager.findDirectory(sourceRoot)
                    if (directory != null && isParent(file, directory)) {
                        file = directory
                        break
                    }
                }
            }
            packageName.split('.').forEach { part ->
                file = file.findSubdirectory(part) ?: file.createSubdirectory(part)
            }

            return file
        }
    }
}
