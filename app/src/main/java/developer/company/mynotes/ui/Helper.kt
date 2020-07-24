package developer.company.mynotes.ui

import android.content.Context
import android.widget.Toast

fun Context.toast(messeage:String) =
    Toast.makeText(this,messeage,Toast.LENGTH_SHORT).show()