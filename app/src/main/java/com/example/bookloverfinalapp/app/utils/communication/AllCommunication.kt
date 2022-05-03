package com.example.bookloverfinalapp.app.utils.communication

import com.example.bookloverfinalapp.app.base.Abstract
import com.example.bookloverfinalapp.app.models.BookAdapterModel
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.BookThatReadAdapterModel
import com.example.bookloverfinalapp.app.models.StudentAdapterModel
import com.example.bookloverfinalapp.app.utils.event.Event
import com.example.bookloverfinalapp.app.utils.navigation.NavigationCommand

interface BooksCommunication : Communication<List<BookAdapterModel>>, Abstract.Mapper {
    class Base : Communication.Base<List<BookAdapterModel>>(), BooksCommunication
}

interface StudentCommunication : Communication<List<StudentAdapterModel>>, Abstract.Mapper {
    class Base : Communication.Base<List<StudentAdapterModel>>(), StudentCommunication
}

interface BooksThatReadCommunication : Communication<List<BookThatRead>>, Abstract.Mapper {
    class Base : Communication.Base<List<BookThatRead>>(), BooksThatReadCommunication
}

interface BooksThatReadAdapterCommunication : Communication<List<BookThatReadAdapterModel>>,
    Abstract.Mapper {
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

interface NetworkErrorCommunication : Communication<Event<Boolean>> {
    class Base : Communication.Base<Event<Boolean>>(), NetworkErrorCommunication
}
