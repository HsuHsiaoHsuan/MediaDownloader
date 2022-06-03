package idv.hsu.media.downloader.ext

private fun String.removeFileSeparator() =
    this.replace(System.getProperty("file.separator") ?: "/", "_", true)

private fun String.removeIllegalFileName() = this.replace("[\\\\/&:*#?\"<>|%]".toRegex(), "_")

private fun String.removeEmoji() =
    this.replace("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\s]".toRegex(), "_")

fun String.reformatFileName() =
    this.removeFileSeparator().removeIllegalFileName().removeEmoji()