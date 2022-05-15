package com.example.bookloverfinalapp.app.utils.communication

import com.example.bookloverfinalapp.app.models.*
import com.example.bookloverfinalapp.app.utils.event.Event
import com.example.bookloverfinalapp.app.utils.navigation.NavigationCommand

interface BooksAdapterModelCommunication : Communication<List<BookAdapterModel>> {
    class Base : Communication.Base<List<BookAdapterModel>>(), BooksAdapterModelCommunication
}

interface BooksCommunication : Communication<List<Book>> {
    class Base : Communication.Base<List<Book>>(), BooksCommunication
}

interface ClassesCommunication : Communication<List<SchoolClass>> {
    class Base : Communication.Base<List<SchoolClass>>(), ClassesCommunication
}


interface BooksQuestionCommunication : Communication<List<BookQuestion>> {
    class Base : Communication.Base<List<BookQuestion>>(), BooksQuestionCommunication
}

interface ClassAdapterCommunication : Communication<List<ClassAdapterModel>> {
    class Base : Communication.Base<List<ClassAdapterModel>>(), ClassAdapterCommunication
}

interface StudentsCommunication : Communication<List<Student>> {
    class Base : Communication.Base<List<Student>>(), StudentsCommunication
}

interface StudentCommunication : Communication<List<StudentAdapterModel>> {
    class Base : Communication.Base<List<StudentAdapterModel>>(), StudentCommunication
}

interface BooksThatReadCommunication : Communication<List<BookThatRead>> {
    class Base : Communication.Base<List<BookThatRead>>(), BooksThatReadCommunication
}

interface BooksThatReadAdapterCommunication : Communication<List<BookThatReadAdapterModel>> {
    class Base : Communication.Base<List<BookThatReadAdapterModel>>(),
        BooksThatReadAdapterCommunication
}

interface NavigationCommunication : Communication<Event<NavigationCommand>> {
    class Base : Communication.Base<Event<NavigationCommand>>(), NavigationCommunication
}

interface ProgressCommunication : Communication<Event<Boolean>> {
    class Base : Communication.Base<Event<Boolean>>(), ProgressCommunication
}

interface ProgressDialogCommunication : Communication<Event<Boolean>> {
    class Base : Communication.Base<Event<Boolean>>(), ProgressDialogCommunication
}

interface ErrorCommunication : Communication<Event<String>> {
    class Base : Communication.Base<Event<String>>(), ErrorCommunication
}

