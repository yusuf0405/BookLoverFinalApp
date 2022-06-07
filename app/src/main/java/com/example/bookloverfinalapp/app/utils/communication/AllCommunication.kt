package com.example.bookloverfinalapp.app.utils.communication

import com.example.bookloverfinalapp.app.ui.adapter.custom.ItemUi
import com.example.bookloverfinalapp.app.models.Book
import com.example.bookloverfinalapp.app.models.BookThatRead
import com.example.bookloverfinalapp.app.models.SchoolClass
import com.example.bookloverfinalapp.app.models.Student
import com.example.bookloverfinalapp.app.ui.admin_screens.screen_school_progress.FragmentSchoolProgressViewModel
import com.example.bookloverfinalapp.app.ui.general_screens.screen_progress.FragmentProgressViewModel
import com.example.bookloverfinalapp.app.utils.event.Event
import com.example.bookloverfinalapp.app.utils.navigation.NavigationCommand
import com.example.domain.models.SchoolDomain


interface BooksCommunication : Communication<List<Book>> {
    class Base : Communication.Base<List<Book>>(), BooksCommunication
}

interface ClassesCommunication : Communication<List<SchoolClass>> {
    class Base : Communication.Base<List<SchoolClass>>(), ClassesCommunication
}

interface ItemUiCommunication : Communication<List<ItemUi>> {
    class Base : Communication.Base<List<ItemUi>>(), ItemUiCommunication
}

interface SchoolProgressCommunication :
    Communication<FragmentSchoolProgressViewModel.SchoolProgress> {
    class Base : Communication.Base<FragmentSchoolProgressViewModel.SchoolProgress>(),
        SchoolProgressCommunication
}

interface SchoolsCommunication : Communication<List<SchoolDomain>> {
    class Base : Communication.Base<List<SchoolDomain>>(), SchoolsCommunication
}

interface SchoolsErrorCommunication : Communication<Event<String>> {
    class Base : Communication.Base<Event<String>>(), SchoolsErrorCommunication
}

interface ClassErrorCommunication : Communication<Event<String>> {
    class Base : Communication.Base<Event<String>>(), ClassErrorCommunication
}

interface ClassStatisticsCommunication : Communication<FragmentProgressViewModel.ClassStatistics> {
    class Base : Communication.Base<FragmentProgressViewModel.ClassStatistics>(),
        ClassStatisticsCommunication
}

interface BooksThatReadCommunication : Communication<List<BookThatRead>> {
    class Base : Communication.Base<List<BookThatRead>>(), BooksThatReadCommunication
}

interface NavigationCommunication : Communication<Event<NavigationCommand>> {
    class Base : Communication.Base<Event<NavigationCommand>>(), NavigationCommunication
}

interface ProgressCommunication : Communication<Event<Boolean>> {
    class Base : Communication.Base<Event<Boolean>>(), ProgressCommunication
}

interface ProgressDialogCommunication : Communication<Boolean> {
    class Base : Communication.Base<Boolean>(), ProgressDialogCommunication
}

interface ErrorCommunication : Communication<Event<String>> {
    class Base : Communication.Base<Event<String>>(), ErrorCommunication
}

interface StudentsCommunication : Communication<List<Student>> {
    class Base : Communication.Base<List<Student>>(), StudentsCommunication
}
