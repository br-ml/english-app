package com.example.englishbg.data

data class ThereIsAreSentence(
    val after: String,
    val isPlural: Boolean,
    val hint: String,
)

data class LikesItem(
    val subject: String,
    val food: String,
    val foodEmoji: String,
    val likes: Boolean,
)

val LIKES_SENTENCES: List<LikesItem> = listOf(
    LikesItem("Tony", "carrots", "🥕", false),
    LikesItem("Ben", "apples", "🍎", true),
    LikesItem("Mary", "cheese", "🧀", false),
    LikesItem("Monica", "bananas", "🍌", true),
    LikesItem("Bill", "bread", "🍞", false),
    LikesItem("Tim", "fish", "🐟", true),
    LikesItem("Emma", "carrots", "🥕", false),
    LikesItem("Mark", "cake", "🎂", true),
    LikesItem("Suzy", "tomatoes", "🍅", false),
    LikesItem("Dan", "sausages", "🌭", true),
    LikesItem("Pam", "apples", "🍎", false),
    LikesItem("Toby", "bananas", "🍌", true),
    LikesItem("The zebra", "apples", "🍎", true),
    LikesItem("The monkey", "bananas", "🍌", true),
    LikesItem("The tiger", "sausages", "🌭", true),
    LikesItem("The snake", "carrots", "🥕", false),
)

data class ImperativeItem(
    val bulgarian: String,
    val correct: String,
    val wrong: String,
)

val IMPERATIVE_SENTENCES: List<ImperativeItem> = listOf(
    ImperativeItem("Стани!", "Stand up.", "Sit down."),
    ImperativeItem("Седни!", "Sit down.", "Stand up."),
    ImperativeItem("Отвори книгите!", "Open your books.", "Close your books."),
    ImperativeItem("Затвори книгите!", "Close your books.", "Open your books."),
    ImperativeItem("Не ставай!", "Don't stand up.", "Stand up."),
    ImperativeItem("Не сядай!", "Don't sit down.", "Sit down."),
    ImperativeItem("Не отваряй книгите!", "Don't open your books.", "Open your books."),
    ImperativeItem("Не затваряй книгите!", "Don't close your books.", "Close your books."),
    ImperativeItem("Подай ми линийката!", "Pass me your ruler.", "Pass me your pencil."),
    ImperativeItem("Сложи молива в несесера!", "Put your pencil in your pencil case.", "Put your book in your bag."),
    ImperativeItem("Сложи книгата в чантата!", "Put your book in your bag.", "Open your bag."),
    ImperativeItem("Не отваряй чантата!", "Don't open your bag.", "Put your bag here."),
)

val THERE_IS_ARE_SENTENCES: List<ThereIsAreSentence> = listOf(
    // Singular → "is"
    ThereIsAreSentence("a cat on the desk.", false, "Има котка на чина."),
    ThereIsAreSentence("a dog in the kitchen.", false, "Има куче в кухнята."),
    ThereIsAreSentence("an apple in the bag.", false, "Има ябълка в чантата."),
    ThereIsAreSentence("a book in the bag.", false, "Има книга в чантата."),
    ThereIsAreSentence("a fish in the lake.", false, "Има риба в езерото."),
    ThereIsAreSentence("a banana on the desk.", false, "Има банан на чина."),
    ThereIsAreSentence("a cake in the kitchen.", false, "Има торта в кухнята."),
    ThereIsAreSentence("a pencil in the bag.", false, "Има молив в чантата."),
    ThereIsAreSentence("a frog in the lake.", false, "Има жаба в езерото."),
    ThereIsAreSentence("a kind answer.", false, "Има добър отговор."),
    ThereIsAreSentence("a famous cat.", false, "Има известна котка."),
    ThereIsAreSentence("a delicious pie in the kitchen.", false, "Има вкусен пай в кухнята."),
    ThereIsAreSentence("a different answer.", false, "Има различен отговор."),
    // Plural → "are"
    ThereIsAreSentence("cats on the desk.", true, "Има котки на чина."),
    ThereIsAreSentence("dogs in the kitchen.", true, "Има кучета в кухнята."),
    ThereIsAreSentence("books on the desk.", true, "Има книги на чина."),
    ThereIsAreSentence("apples in the bag.", true, "Има ябълки в чантата."),
    ThereIsAreSentence("bananas on the desk.", true, "Има банани на чина."),
    ThereIsAreSentence("different answers.", true, "Има различни отговори."),
    ThereIsAreSentence("delicious cakes in the kitchen.", true, "Има вкусни торти в кухнята."),
    ThereIsAreSentence("pencils in the pencil case.", true, "Има моливи в несесера."),
    ThereIsAreSentence("frogs in the lake.", true, "Има жаби в езерото."),
    ThereIsAreSentence("kind answers.", true, "Има добри отговори."),
    ThereIsAreSentence("famous cats.", true, "Има известни котки."),
    ThereIsAreSentence("carrots in the bag.", true, "Има моркови в чантата."),
)
