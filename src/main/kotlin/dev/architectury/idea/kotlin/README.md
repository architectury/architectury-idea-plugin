This directory contains the (partial) Kotlin support code
in the Architectury IDEA plugin.

Some things you might want to note here:

- IDEA reports errors when you call the Kotlin plugin's functions
  that have standard PSI types in their signature. I have no clue why.
- Kotlin PSI element -> Java PSI element: use `LightClassUtil`
