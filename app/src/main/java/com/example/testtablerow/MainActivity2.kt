package com.example.testtablerow

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.github.irshulx.Editor
import com.github.irshulx.EditorListener
import com.github.irshulx.WatchListener
import com.github.irshulx.models.EditorContent


class MainActivity2 : AppCompatActivity() {
    private lateinit var renderrr : Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        renderrr = findViewById(R.id.renderer)
        val intent = intent.getStringExtra("content")
        //val t = renderrr1.contentAsSerialized
       // Log.i("TAGGGGO", "onCreate: $t")


        //val txt = renderrr.contentAsSerialized
       /* val htmlForm = Editor(this, null)
        val htmlFormat = htmlForm.getContentDeserialized(intent)*/

      //  val Deserialized: EditorContent = renderrr.getContentDeserialized(renderrr1.contentAsSerialized)
     //   val htmlForm = Editor(this, null)
       // val htmlFormat = htmlForm.getContentAsHTML(Deserialized)


       // Log.i("TAGGGGO", "onCreate: $htmlFormat")
        renderrr.editorListener = object : EditorListener {
            override fun onUpload(image: Bitmap?, uuid: String?) {
                Log.i("TAGGGGO", "onCreate:")
            }

            override fun onRenderMacro(name: String?, props: MutableMap<String, Any>?, index: Int): View {
                return layoutInflater.inflate(R.layout.layout_authored_by,null)
            }

            override fun onTextChanged(editText: EditText?, text: Editable?) {
            }

        }
        renderrr.watchListener = WatchListener { url->
            Log.i("TAGGGGGGGOO", "onCreate: $url")
          /*  val intent = Intent(requireContext(), VideoPlayerActivity::class.java)
            intent.putExtra("url", url)
            startActivity(intent)*/
        }
       //renderrr.render(Deserialized)
        renderrr.render(intent.toString())
    }
}