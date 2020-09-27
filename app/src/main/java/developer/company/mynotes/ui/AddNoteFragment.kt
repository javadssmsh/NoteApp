package developer.company.mynotes.ui

import android.app.AlertDialog
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.Navigation

import developer.company.mynotes.R
import developer.company.mynotes.db.Note
import developer.company.mynotes.db.NoteDatabase
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.launch


class AddNoteFragment : BaseFragment() {

    private var note: Note? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        arguments?.let {
            note = AddNoteFragmentArgs.fromBundle(it).note
            edt_title.setText(note?.title)
            edt_not.setText(note?.note)
        }

        btn_save.setOnClickListener { view ->

            val noteTittle = edt_title.text.toString().trim()
            val noteBody = edt_not.text.toString().trim()

            if (noteTittle.isEmpty()) {
                edt_title.error = "tittle required"
                edt_title.requestFocus()
                return@setOnClickListener
            }

            if (noteBody.isEmpty()) {
                edt_not.error = "Body required"
                edt_not.requestFocus()
                return@setOnClickListener
            }

            launch {
                context?.let {
                    val mNote = Note(noteTittle, noteBody)

                    if (note == null) {
                        NoteDatabase(it).getNoteDao().addNote(mNote)
                        it.toast("Note saved")
                    } else {
                        mNote.id = note!!.id
                        NoteDatabase(it).getNoteDao().updateNote(mNote)
                        it.toast("Note updated")
                    }

                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(view).navigate(action)
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete -> if (note!=null){
                deleteNote()
            }else{
                context?.toast("Can not delete")
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun deleteNote() {
        AlertDialog.Builder(context).apply {
            setTitle("Are you sure ? ")
            setMessage("You can not undo this operation")
            setPositiveButton("Yes"){_,_ ->
                launch {
                    NoteDatabase(context).getNoteDao().deleteNote(note!!)
                    val action = AddNoteFragmentDirections.actionSaveNote()
                    Navigation.findNavController(requireView()).navigate(action)
                }
            }
            setNegativeButton("No"){_,_->

            }
        }.create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu, menu)
    }

}
