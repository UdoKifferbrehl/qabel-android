package de.qabel.qabelbox.box.interactor

import de.qabel.qabelbox.box.dto.BoxPath
import de.qabel.qabelbox.box.dto.BrowserEntry
import de.qabel.qabelbox.box.dto.DownloadSource
import de.qabel.qabelbox.box.dto.UploadSource
import de.qabel.qabelbox.box.provider.DocumentId
import rx.Observable

interface FileBrowser {
    fun upload(path: BoxPath.File, source: UploadSource): Observable<Unit>
    fun download(path: BoxPath.File):  Observable<DownloadSource>
    fun delete(path: BoxPath):  Observable<Unit>
    fun list(path: BoxPath.FolderLike): Observable<List<BrowserEntry>>
    fun createFolder(path: BoxPath.FolderLike): Observable<Unit>
    fun query(path: BoxPath): Observable<BrowserEntry>
    fun asDocumentId(path: BoxPath): Observable<DocumentId>
}

