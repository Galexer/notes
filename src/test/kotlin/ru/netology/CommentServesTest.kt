package ru.netology

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.netology.CommentServes.add
import ru.netology.CommentServes.delete
import ru.netology.CommentServes.edit
import ru.netology.CommentServes.restoreComments

class CommentServesTest {
    @Before
    fun clearBeforeTest() {
        CommentServes.clear()
        NotesService.clear()
    }

    @Test
    fun add() {
        val note1 = Note(10, 1, "note", "note text", 0, 0)
        NotesService.add(note1)
        val comment1 = Comment(1,10, 1, 1, "comment")
        val result = add(comment1)

        assertEquals(1, result.commentId)
    }

    @Test(expected = NoteNotFoundException::class)
    fun addException() {
        val comment1 = Comment(1,10, 1, 1, "comment")
        val result = add(comment1)
    }

    @Test
    fun delete() {
        val note1 = Note(10, 1, "note", "note text", 0, 0)
        NotesService.add(note1)
        val comment1 = Comment(1,10, 1, 1, "comment")
        add(comment1)

        val result = delete(1)
        assertTrue(result)
    }

    @Test(expected = CommNotDeletedException::class)
    fun deleteException() {
        delete(1)
    }

    @Test
    fun edit() {
        val note1 = Note(10, 1, "note", "note text", 0, 0)
        NotesService.add(note1)
        val comment1 = Comment(1,10, 1, 1, "comment")
        add(comment1)
        val commentEdit = Comment(1,1, 1, 1, "comment edited")

        val result = edit(commentEdit)
        assertTrue(result)
    }

    @Test (expected = CommNotFoundException::class)
    fun editException() {
        val commentEdit = Comment(1,1, 1, 1, "comment edited")

        edit(commentEdit)
    }

    @Test
    fun getComments() {
        val note1 = Note(10, 1, "note", "note text", 0, 0)
        NotesService.add(note1)
        val comment1 = Comment(1,10, 1, 1, "comment")
        add(comment1)
        val comment2 = Comment(1,10, 1, 1, "comment2")
        add(comment2)
        val pairList = listOf<Pair<Int, Comment>>(1 to comment1.copy(commentId = 1), 2 to comment2.copy(commentId = 2))

        val result = CommentServes.getComments()
        assertEquals(pairList, result)
    }

    @Test
    fun restoreComments() {
        val note1 = Note(10, 1, "note", "note text", 0, 0)
        NotesService.add(note1)
        val comment1 = Comment(1,10, 1, 1, "comment")
        add(comment1)
        delete(1)

        val result = restoreComments(1)
        assertTrue(result)
    }

    @Test(expected = NoCommForRestoreException::class)
    fun restoreCommentsException() {
        restoreComments(1)
    }
}