package ru.netology

fun main(args: Array<String>) {
    println("Hello World!")
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
    protected var notes = mutableMapOf<Int, Note>()
    protected var noteId = 0
    protected var comments = mutableMapOf<Int, Comment>()
    protected var commentIds = 0
    protected var deletedComments = mutableMapOf<Int, Comment>()
    open fun addThis (think: T): T{
        return think
    }
    open fun deleteThis(thing: T): Boolean{
        return false
    }
    open fun editThis(thing: T): Boolean{
        return false
    }
}

object NotesService: CRUDFun<Note>() {
    override fun addThis(note: Note): Note {
        val noteNew = note.copy(id = noteId)
        notes[noteId] = noteNew
        noteId++
        return notes[note.id] ?: throw NoAddedNoteException("Note has not added")
    }

    override fun deleteThis(note: Note) = notes.remove(note.id, note)

    override fun editThis(note: Note): Boolean {
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
    override fun addThis(comment: Comment): Comment {
        if(notes.containsKey(comment.noteId)) {
            val comNew = comment.copy(commentId = commentIds)
            comments.put(comNew.commentId, comNew)
            commentIds++
            return comments[comNew.commentId]!!
        }
        throw NoteNotFoundException("Comment not added for this note, because note not found")
    }

    override fun deleteThis(comment: Comment): Boolean {
        if(comments.containsKey(comment.commentId)){
            deletedComments += comment.commentId to comment
            return comments.remove(comment.commentId, comment)
        }
        throw CommNotDeletedException("Comment not deleted or not already exist")
    }

    override fun editThis(comment: Comment): Boolean {
        if(comments.containsKey(comment.commentId)) {
            comments[comment.commentId] = comment
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

class NoAddedNoteException(text: String): RuntimeException(text)
class NoteNotFoundException(text: String): RuntimeException(text)
class CommNotDeletedException(text: String): RuntimeException(text)
class CommNotFoundException(text: String): RuntimeException(text)
class NoCommForRestoreException(text: String): RuntimeException(text)