package com.example.testtablerow

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.irshulx.Editor
import com.github.irshulx.EditorListener
import com.github.irshulx.WatchListener
import com.github.irshulx.models.EditorTextStyle
import java.io.IOException

class MainActivity : AppCompatActivity() {
private lateinit var editor: Editor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
         editor = findViewById<Editor>(R.id.editor)


        findViewById<View>(R.id.btnBUt).setOnClickListener {
            //val text = editor.contentAsSerialized
            val text = editor.contentAsHTML

            //val htmlForm = Editor(this, null)
            //val htmlFormat = htmlForm.getContentAsHTML(text)
          /*  val t = TextUtils.htmlEncode(text)
            Log.i("TAGGO", "onCreate: $t")
            editor.render(t)
            val to = editor.contentAsSerialized
            Log.i("TAGGG", "onCreate: $to")
*/
            Log.i("TAGGG", "onCreate: $text")
            val intent = Intent(this@MainActivity,MainActivity2::class.java)
            intent.putExtra("content",text)
            startActivity(intent)
        }
        findViewById<View>(R.id.actionInsertImage).setOnClickListener { editor.openImagePicker() }

       /* findViewById<View>(R.id.actionInsertLink).setOnClickListener {
            editor.insertLink()
        }*/
        findViewById<View>(R.id.actionInsertLink).setOnClickListener {
            //https://www.youtube.com/watch?v=8U-zDpwm1Rk
            editor.insertImageForVideo("https://aws-mhs-bucket.s3-ap-southeast-1.amazonaws.com/auth/images/user-profiles/2928.png",
            "https://www.youtube.com/watch?v=8U-zDpwm1Rk")
        }


        findViewById<View>(R.id.actionErase).setOnClickListener { editor.clearAllContents() }
        findViewById<View>(R.id.action_Header).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.H1
            )
        }
        //editor.setNormalTextSize(10);
        // editor.setEditorTextColor("#FF3333");
        //editor.StartEditor();
        editor.watchListener = WatchListener { url -> Log.i("TAGGGO", "onWatch: $url") }
        editor.editorListener = object : EditorListener {
            override fun onTextChanged(editText: EditText, text: Editable) {
                // Toast.makeText(EditorTestActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            override fun onUpload(image: Bitmap, uuid: String) {
                Toast.makeText(this@MainActivity, uuid, Toast.LENGTH_LONG).show()

                editor.onImageUploadComplete(
                    "http://www.videogamesblogger.com/wp-content/uploads/2015/08/metal-gear-solid-5-the-phantom-pain-cheats-640x325.jpg",
                    uuid
                )
                // editor.onImageUploadFailed(uuid);
            }

            override fun onRenderMacro(
                name: String,
                props: Map<String, Any>,
                index: Int
            ): View {
                return layoutInflater.inflate(R.layout.layout_authored_by, null)
            }
        }
        findViewById<View>(R.id.action_Header2).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.H2
            )
        }

        findViewById<View>(R.id.action_Header3).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.H3
            )
        }

        findViewById<View>(R.id.action_Bold).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.BOLD
            )
        }

        findViewById<View>(R.id.actionItalic).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.ITALIC
            )
        }

        findViewById<View>(R.id.actionIndent).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.INDENT
            )
        }

        findViewById<View>(R.id.actionBlockquote).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.BLOCKQUOTE
            )
        }

        findViewById<View>(R.id.actionOutdent).setOnClickListener {
            editor.updateTextStyle(
                EditorTextStyle.OUTDENT
            )
        }

        findViewById<View>(R.id.actionBulleted).setOnClickListener { editor.insertList(false) }

        findViewById<View>(R.id.actionUnorderedNumbered).setOnClickListener {
            editor.insertList(
                true
            )
        }

        findViewById<View>(R.id.actionHr).setOnClickListener { editor.insertDivider() }


        /*findViewById<View>(R.id.actionColor).setOnClickListener {
            Builder(this@EditorTestActivity)
                .initialColor(Color.RED) // Set initial color
                .enableAlpha(true) // Enable alpha slider or not
                .okTitle("Choose")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .showValue(true)
                .build()
                .show(findViewById(android.R.id.content), object : ColorPickerObserver() {
                    fun onColorPicked(color: Int) {
                        Toast.makeText(
                            this@EditorTestActivity,
                            "picked" + colorHex(color),
                            Toast.LENGTH_LONG
                        ).show()
                        editor.updateTextColor(colorHex(color))
                    }

                    fun onColor(color: Int, fromUser: Boolean) {}
                })
        }*/
    }
    /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                editor.insertImage(bitmap);
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            //Write your code if there's no result
            Toast.makeText(getApplicationContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            // editor.RestoreState();
        }
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == editor.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK &&
                data != null && data.data != null){
            val uri = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                editor.insertImage(bitmap)
               // editor.insertImageForVideo(bitmap)
            }catch (e : IOException){
                e.printStackTrace()
            }
        }
    }

}