package alex.pakshin.ru.netology.nmedia.db

object PostsTable {
    const val NAME = "posts"

    val DDL = """
        CREATE TABLE $NAME (
            ${Column.ID.columnName} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${Column.Author.columnName} TEXT NOT NULL,
            ${Column.Content.columnName} TEXT NOT NULL,
            ${Column.Published.columnName} TEXT NOT NULL,
            ${Column.Liked.columnName} BOOLEAN NOT NULL DEFAULT 0,
            ${Column.LikeCount.columnName} INTEGER NOT NULL DEFAULT 0,
            ${Column.ShareCount.columnName} INTEGER NOT NULL DEFAULT 0,
            ${Column.URL.columnName} TEXT DEFAULT NULL
        )
    """.trimIndent()

    val ALL_COLUMNS_NAMES = Column.values().map {
        it.columnName
    }.toTypedArray()

    enum class Column(val columnName: String) {
        ID("id"),
        Author("author"),
        Content("content"),
        Published("published"),
        Liked("liked"),
        LikeCount("likeCount"),
        ShareCount("shareCount"),
        URL("url")
    }

}