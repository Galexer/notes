package ru.netology

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.netology.NotesService.add
import ru.netology.NotesService.delete
import ru.netology.NotesService.edit
import ru.netology.NotesService.getById

class NotesServiceTest {

    @Test
    fun add() {
        val note1 = Note(10, 1, "note", "note text", 0, 0)

        val result = add(note1)
        assertEquals(1, result.id)
    }

    @Before
    fun clearBeforeTest() {
        NotesService.clear()
    }

    @Test
    fun delete() {
        val note1 = Note(10, 1, "note", "note text", 0, 0)
        add(note1)

        val result = delete(1)
        assertTrue(result)
    }

    @Test(expected = NoteNotFoundException::class)
    fun deleteThrowException() {
        val note1 = Note(10, 1, "note", "note text", 0, 0)
        add(note1)

        delete(10)
    }

    @Test
    fun edit() {
        val note1 = Note(10, 1, "note", "note text", 0, 0)
        add(note1)
        val editedNote1 = Note(1, 1, "note new title", "note new text", 1, 1)

        var result = edit(editedNote1)
        assertTrue(result)
    }

    @Test(expected = NoteNotFoundException::class)
    fun editException() {
        val note1 = Note(10, 1, "note", "note text", 0, 0)
        add(note1)
        val editedNote1 = Note(10, 1, "note new title", "note new text", 1, 1)

        edit(editedNote1)
    }

    @Test
    fun getAll() {
        val note1 = Note(10, 1, "note", "note text", 0, 0)
        val note2 = Note(10, 1, "note2", "note text2", 0, 0)
        add(note1)
        add(note2)
        val pairList = listOf<Pair<Int, Note>>(1 to note1.copy(id = 1), 2 to note2.copy(id = 2))

        val result = NotesService.getAll()
        assertEquals(result, pairList)
    }

    @Test
    fun getById() {
        val note1 = Note(10, 1, "note", "note text", 0, 0)
        add(note1)

        val result = getById(1)
        assertEquals(1, result.id)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getByIdException() {
        val note1 = Note(10, 1, "note", "note text", 0, 0)
        add(note1)

        getById(10)
    }
}