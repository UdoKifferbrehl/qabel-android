package de.qabel.qabelbox.interactor

import de.qabel.core.config.Contact
import de.qabel.qabelbox.dto.ChatMessage
import rx.Observable
import rx.lang.kotlin.observable

class MockChatUseCase(override val contact: Contact, var messages: List<ChatMessage>): ChatUseCase {
    override fun send(text: String): Observable<ChatMessage> {
        throw UnsupportedOperationException()
    }

    override fun retrieve() = observable<List<ChatMessage>> { it.onNext(messages); it.onCompleted() }
}
