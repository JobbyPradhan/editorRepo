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
    private lateinit var renderrr1 : Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        renderrr = findViewById(R.id.renderer)
        renderrr1 = findViewById(R.id.renderer1)
        val intent = intent.getStringExtra("content")
        val text ="<div data-tag=\"iframe\"><img src=\"https://aws-mhs-bucket.s3-ap-southeast-1.amazonaws.com/auth/images/user-profiles/2928.png\" /><p data-tag=\"img-sub\" style=\"color:#5E5E5E;\" class=\"editor-image-subtitle\">https://www.youtube.com/watch?v=8U-zDpwm1Rk</p></div><p data-tag=\"input\" style=\"color:#000000;\"></p>"
        renderrr1.render(text)
        //val t = renderrr1.contentAsSerialized
       // Log.i("TAGGGGO", "onCreate: $t")
      //  renderrr.render(intent.toString())

        //val txt = renderrr.contentAsSerialized
       /* val htmlForm = Editor(this, null)
        val htmlFormat = htmlForm.getContentDeserialized(intent)*/

      //  val Deserialized: EditorContent = renderrr.getContentDeserialized(renderrr1.contentAsSerialized)
     //   val htmlForm = Editor(this, null)
       // val htmlFormat = htmlForm.getContentAsHTML(Deserialized)


       // Log.i("TAGGGGO", "onCreate: $htmlFormat")
        renderrr.editorListener = object : EditorListener {
            override fun onUpload(image: Bitmap?, uuid: String?) {
            }

            override fun onRenderMacro(name: String?, props: MutableMap<String, Any>?, index: Int): View {
                return layoutInflater.inflate(R.layout.layout_authored_by,null)
            }

            override fun onTextChanged(editText: EditText?, text: Editable?) {
            }

        }
        renderrr.watchListener = WatchListener { url->
          /*  val intent = Intent(requireContext(), VideoPlayerActivity::class.java)
            intent.putExtra("url", url)
            startActivity(intent)*/
        }
       //renderrr.render(Deserialized)
    }
}