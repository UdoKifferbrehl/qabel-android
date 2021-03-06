package de.qabel.qabelbox.box.views

import de.qabel.qabelbox.box.dto.BrowserEntry
import de.qabel.qabelbox.box.provider.DocumentId

interface FileBrowserView {
    fun showEntries(entries: List<BrowserEntry>)
    fun open(documentId: DocumentId)
    fun share(documentId: DocumentId)
    fun export(documentId: DocumentId)
    fun showError(throwable: Throwable)
    fun refreshDone()
    fun refreshStart()
}

