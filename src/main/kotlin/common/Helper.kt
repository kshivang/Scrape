package common

import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

fun List<List<String>>.writeToCSV(filePath: String, append: Boolean = false) = csvWriter().writeAll(this, filePath, append)
