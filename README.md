sbt-rewarn
==================
Always display compilation warnings, even for unchanged files.

By default, `sbt` shows compilation warnings only for the source files that have been re-compiled in the current incremental compilation run. `sbt-rewarn` makes it print all warnings on every compilation, even if you didn't change anything.

This is particularly helpful if you want to keep your project warning-free. Simply enabling `-Xfatal-warnings` can be too annoying during development, especially the `-Wunused:*` family of warnings. The better alternative is to make warnings fatal on CI but not locally. Together with this plugin, you will always see if you have introduced any warnings, regardless of the incremental compilation.

![sbt-rewarn](https://user-images.githubusercontent.com/831307/88443498-d49fc980-ce18-11ea-8154-8245ceb309c1.png)

Requirements
==============
sbt 1.0.0 or later, with an exception of sbt 1.4.0 (sbt 1.4.1+ is supported again).

Installation
============

The plugin supports both global and per-project installation. 
If you want this plugin to be available for all `sbt` projects you work with, configure it in `~/.sbt/1.0/plugins/plugins.sbt`.
If you want to enable this plugin for some specific project only, configure it in `<project-root>/project/plugins.sbt`.

### Stable version
Add the following line to the `plugins.sbt` file:

```
addSbtPlugin("com.timushev.sbt" % "sbt-rewarn" % "x.x.x")
```

The latest version is ![Bintray](https://img.shields.io/bintray/v/rtimush/sbt-plugins/sbt-rewarn?color=blue&label=sbt-rewarn&style=plastic)

No additional configuration is necessary, the plugin will be automatically activated.

### Snapshot version
Choose one of versions available on [BinTray](https://bintray.com/rtimush/sbt-plugin-snapshots/sbt-rewarn/view)
or the [latest](https://bintray.com/rtimush/sbt-plugin-snapshots/sbt-rewarn/_latestVersion) one. Then add the following lines to the `plugins.sbt` file:

```
resolvers += Resolver.bintrayIvyRepo("rtimush", "sbt-plugin-snapshots")
addSbtPlugin("com.timushev.sbt" % "sbt-rewarn" % "x.x.x-y-zzzzzzz")
```

Note, that snapshots are not updated automatically.
