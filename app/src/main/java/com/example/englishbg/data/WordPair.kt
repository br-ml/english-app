package com.example.englishbg.data

data class WordPair(val english: String, val bulgarian: String)
data class Category(val name: String, val words: List<WordPair>)
