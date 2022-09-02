package ru.netology

fun main() {
}

data class Note(
    val id: Int,
    val title: String,
    val text: String,
    val privacy: Int,
    val commentPrivacy: Int
)

data class Comment(
    val noteId: Int,
    val commentId: Int,
    val ownerId: Int,
    val replayTo: Int,
    val message: String
)

abstract class CRUDFun <T> {
    open fun add (thing: T): T{
        return thing
    }
    open fun delete(id: Int): Boolean{
        return false
    }
    open fun edit(thing: T): Boolean{
        return false
    }
}

object NotesService: CRUDFun<Note>() {
    var notes = mutableMapOf<Int, Note>()
    var noteId = 0
    fun clear() {
        notes = mutableMapOf<Int, Note>()
        noteId = 0
    }
    override fun add(note: Note): Note {
        noteId++
        val noteNew = note.copy(id = noteId)
        notes[noteId] = noteNew
        return notes[noteId]!!
    }

    override fun delete(id: Int): Boolean {
        if(notes.containsKey(id)){
            return notes.remove(notes[id]?.id, notes[id])
        }
        throw NoteNotFoundException("Note with this ID not found")
    }

    override fun edit(note: Note): Boolean {
        if(notes.containsKey(note.id)) {
            notes[note.id] = note
            return true
        }
        throw NoteNotFoundException("Note with this ID not found")
    }

    fun getAll() = notes.toList()

    fun getById(id: Int) = notes[id] ?: throw NoteNotFoundException("Note with this ID not found")
}
object CommentServes: CRUDFun<Comment>(){
    var comments = mutableMapOf<Int, Comment>()
    var commentIds = 0
    var deletedComments = mutableMapOf<Int, Comment>()

    fun clear() {
        comments = mutableMapOf<Int, Comment>()
        commentIds = 0
        deletedComments = mutableMapOf<Int, Comment>()
    }
    override fun add(comment: Comment): Comment {
        if(NotesService.getById(comment.noteId).id == comment.noteId) {
            commentIds++
            val comNew = comment.copy(commentId = commentIds)
            comments[comNew.commentId] = comNew
            return comments[comNew.commentId]!!
        }
        throw CommentNotAddedException("Comment not added")
    }

    override fun delete(id: Int): Boolean {
        if(comments.containsKey(id)){
            deletedComments += comments[id]!!.commentId to comments[id]!!
            return comments.remove(comments[id]?.commentId, comments[id])
        }
        throw CommNotDeletedException("Comment not deleted or not already exist")
    }

    override fun edit(comment: Comment): Boolean {
        if(comments.containsKey(comment.commentId)) {
            comments[comment.commentId] = comment
            return true
        }
        throw CommNotFoundException("Note with this ID not found")
    }

    fun getComments() = comments.toList()
    fun restoreComments(commentId: Int): Boolean{
        val comm = deletedComments[commentId] ?: throw NoCommForRestoreException("Comment with this ID can not be restored")
        comments += comm.commentId to comm
        return true
    }
}

class NoteNotFoundException(text: String): RuntimeException(text)
class CommNotDeletedException(text: String): RuntimeException(text)
class CommNotFoundException(text: String): RuntimeException(text)
class NoCommForRestoreException(text: String): RuntimeException(text)
class CommentNotAddedException(text: String):  RuntimeException(text)