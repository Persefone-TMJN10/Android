<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]

<!-- PROJECT LOGO -->
<br />
<p align="center">
  <a href="https://github.com/github_username/repo">
    <img src="https://image.flaticon.com/icons/svg/605/605255.svg" alt="Logo" width="100" height="100">
  </a>

  <h3 align="center">Persefone Android</h3>

  <p align="center">
    A mobile application to monitor the simulated state of an imaginary nuclear power plant.
    <br />
    <a href="https://docs.google.com/document/d/1MvyMiMswEZONcX7VEhS3iGQ1EV0mUhDZG-rRwyo7Jd0/edit">
      <strong>Project Notes »</strong>
    </a>
    <br />
    <br />
    <a href="https://docs.google.com/document/d/1MvyMiMswEZONcX7VEhS3iGQ1EV0mUhDZG-rRwyo7Jd0/edit">Meeting Log</a>
    ·
    <a href="https://docs.google.com/document/d/1MvyMiMswEZONcX7VEhS3iGQ1EV0mUhDZG-rRwyo7Jd0/edit">Another Log</a>
    ·
    <a href="https://docs.google.com/document/d/1MvyMiMswEZONcX7VEhS3iGQ1EV0mUhDZG-rRwyo7Jd0/edit">A Third Log</a>
  </p>
</p>

<!-- TABLE OF CONTENTS -->
## Table of Contents

* [Built With](#built-with)
* [Code Conventions](#code-conventions)
  * [Naming](#naming)

<!-- Built With -->
### Built With
* [Android Studio](https://developer.android.com/studio)
* [Kotlin](https://kotlinlang.org/docs/reference/android-overview.html)
* [Android Kotlin API](https://developer.android.com/reference/kotlin/packages)
* [Material Design](https://material.io/design/)

<!-- CODE CONVENTIONS -->
## Code Conventions
Repository wide code conventions.

### Naming
```kotlin
/* VARIABLES */
var fooBar = 3.6

val fooBar = "roentgen"

const val FOO_BAR = "inside Chernobyl Nuclear Power Plant"

val btn_shutDown: Button = findViewById(R.id.btn_shutDown) // View object

/* MEMBERS */
var _fooBar = 3.6

val _fooBar = "roentgen"

const val _FOO_BAR = "inside Chernobyl Nuclear Power Plant"

val _btn_shutDown: Button = findViewById(R.id.btn_shutDown) // Memeber view object

/* METHODS */
fun fooBar(fooBar: Int) {
  /*...*/
}
```

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/Persefone-TMJN10/Android.svg?style=flat-square
[contributors-url]: https://github.com/Persefone-TMJN10/Android/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/Persefone-TMJN10/Android.svg?style=flat-square
[forks-url]: https://github.com/Persefone-TMJN10/Android/network/members
[stars-shield]: https://img.shields.io/github/stars/Persefone-TMJN10/Android.svg?style=flat-square
[stars-url]: https://github.com/Persefone-TMJN10/Android/stargazers
[issues-shield]: https://img.shields.io/github/issues/Persefone-TMJN10/Android.svg?style=flat-square
[issues-url]: https://github.com/Persefone-TMJN10/Android/issues
[product-screenshot]: images/screenshot.png
