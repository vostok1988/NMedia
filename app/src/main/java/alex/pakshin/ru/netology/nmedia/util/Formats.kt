package alex.pakshin.ru.netology.nmedia.util

 fun getDecimalFormat(count: Int): String = when {
    count < 1000 -> count.toString()
    (count < 10000 && count % 1000 / 100 == 0) || count in 10000..1000000 -> "${(count / 1000)}K"
    count < 10000 -> "${(count / 100 / 10.0)}K"
    (count < 10000000 && count % 1000000 / 100000 == 0) || count in 10000000..1000000000 -> "${(count / 1000000)}M"
    count < 10000000 -> "${(count / 100000 / 10.0)}M"
    else -> ""
}