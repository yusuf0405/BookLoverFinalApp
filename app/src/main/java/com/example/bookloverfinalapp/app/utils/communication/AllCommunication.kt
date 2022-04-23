package com.example.bookloverfinalapp.app.utils.communication

import com.example.bookloverfinalapp.app.base.Abstract
import com.example.bookloverfinalapp.app.models.BookAdapterModel
import com.example.bookloverfinalapp.app.models.StudentBookAdapterModel
import com.example.bookloverfinalapp.app.utils.event.Event
import com.example.bookloverfinalapp.app.utils.navigation.NavigationCommand

interface BooksCommunication : Communication<List<BookAdapterModel>>, Abstract.Mapper {
    class Base : Communication.Base<List<BookAdapterModel>>(), BooksCommunication
}

interface StudentBooksCommunication : Communication<List<StudentBookAdapterModel>>,
    Abstract.Mapper {
    class Base : Communication.Base<List<StudentBookAdapterModel>>(), StudentBooksCommunication
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
