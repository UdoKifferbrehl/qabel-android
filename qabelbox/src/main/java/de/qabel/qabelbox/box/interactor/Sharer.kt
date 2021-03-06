package de.qabel.qabelbox.box.interactor

import de.qabel.chat.repository.entities.ChatDropMessage
import de.qabel.core.config.Contact
import de.qabel.qabelbox.box.dto.BoxPath
import rx.Observable
import rx.Single

interface Sharer {

    fun sendFileShare(contact: Contact, path: BoxPath): Observable<ChatDropMessage>

    fun revokeFileShare(path: BoxPath): Single<Unit>

}
