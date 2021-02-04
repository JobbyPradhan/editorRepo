package com.github.irshulx.Components;

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.github.irshulx.EditorComponent;
import com.github.irshulx.EditorCore;
import com.github.irshulx.R;
import com.github.irshulx.models.EditorContent;
import com.github.irshulx.models.EditorControl;
import com.github.irshulx.models.EditorType;
import com.github.irshulx.models.HtmlTag;
import com.github.irshulx.models.Node;
import com.github.irshulx.models.RenderType;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class VideoExtensions  extends EditorComponent implements YouTubePlayer.OnInitializedListener {
    private EditorCore editorCore;
    private int editorVideoLayout = R.layout.tmpl_video_view;
    @Override
    public Node getContent(View view) {
        Node node = getNodeInstance(view);
        EditorControl videoTag = (EditorControl) view.getTag();
        if (!TextUtils.isEmpty(videoTag.path)) {
            node.content.add(videoTag.path);
        }
        return node;
    }

    @Override
    public String getContentAsHTML(Node node, EditorContent content) {
        //String subHtml = componentsWrapper.getInputExtensions().getInputHtml(node.childs.get(0));
        String html = componentsWrapper.getHtmlExtensions().getTemplateHtml(node.type);
        html = html.replace("{{$url}}", node.content.get(0));
        //html = html.replace("{{$img-sub}}", subHtml);
        return html;
    }

    @Override
    public void renderEditorFromState(Node node, EditorContent content) {
        String path = node.content.get(0);
        if(editorCore.getRenderType() == RenderType.Renderer) {
            loadVideo(path);
        }else{
            View layout = insertVideo(null,path,editorCore.getChildCount(),node.childs.get(0).content.get(0), false);
            componentsWrapper. getInputExtensions().applyTextSettings(node.childs.get(0), (TextView) layout.findViewById(R.id.desc));
        }
    }
   /* public void youtubeLink(String uri) {
        EditorType editorType = editorCore.getControlType(editorCore.getActiveView());
        EditText editText = (EditText) editorCore.getActiveView();
        if (editorType == EditorType.INPUT || editorType == EditorType.UL_LI) {
            String text = Html.toHtml(editText.getText());
            if (TextUtils.isEmpty(text))
                text = "<p dir=\"ltr\"></p>";
            text = trimLineEnding(text);
            Document _doc = Jsoup.parse(text);
            Elements x = _doc.select("p");
            String existing = x.get(0).html();
            x.get(0).html(existing + "<iframe width=\"100%\" height=\"200\" src=\"www.youtube.com/embed/'"+uri+"'\" frameborder=\"0\" allowfullscreen></iframe>");
            //x.get(0).html(existing + " <a href='" + uri + "'>" + uri + "</a>");
            Spanned toTrim = Html.fromHtml(x.toString());
            CharSequence trimmed = noTrailingwhiteLines(toTrim);
            editText.setText(trimmed);   //
            editText.setSelection(editText.getText().length());
        }
    }*/

    private View insertVideo(String uri, String path, int childCount, String s, boolean b) {
        final View childLayout = ((Activity) editorCore.getContext()).getLayoutInflater().inflate(this.editorVideoLayout, null);
        YouTubePlayerView videoView = childLayout.findViewById(R.id.playerView);
        if (!TextUtils.isEmpty(path)){
            ///loadVideoUsingLib(path,videoView);
        }else{
           // videoView.start();
        }
        final String uuid = generateUUID();
        if (childCount == -1){
            childCount = editorCore.determineIndex(EditorType.iframe);
        }
        showNextInputHint(childCount);
    //    childLayout.setTag(createVideoTag(hasUploaded ? path : uuid));

        if (editorCore.isLastRow(childLayout) && b) {
            componentsWrapper.getInputExtensions().insertEditText(childCount + 1, null, null);
        }
        return childLayout;
    }

    private void showNextInputHint(int index) {
        View view = editorCore.getParentView().getChildAt(index);
        EditorType type = editorCore.getControlType(view);
        if (type != EditorType.INPUT)
            return;
        TextView tv = (TextView) view;
        tv.setHint(editorCore.getPlaceHolder());
        Linkify.addLinks(tv,Linkify.ALL);
    }

    private void hideInputHint(int index) {
        View view = editorCore.getParentView().getChildAt(index);
        EditorType type = editorCore.getControlType(view);
        if (type != EditorType.INPUT)
            return;

        String hint = editorCore.getPlaceHolder();
        if (index > 0) {
            View prevView = editorCore.getParentView().getChildAt(index - 1);
            EditorType prevType = editorCore.getControlType(prevView);
            if (prevType == EditorType.INPUT)
                hint = null;
        }
        TextView tv = (TextView) view;
        tv.setHint(hint);
    }
    public String generateUUID() {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String sdt = df.format(new Date(System.currentTimeMillis()));
        UUID x = UUID.randomUUID();
        String[] y = x.toString().split("-");
        return y[y.length - 1] + sdt;
    }
    public EditorControl createVideoTag(String path) {
        EditorControl control = editorCore.createTag(EditorType.iframe);
        control.path = path;
        return control;
    }
    public void loadVideo(String _path) {
        //String desc = node.content.get(0);
        final View childLayout = loadVideoRemote(_path);
    }
    public void loadVideo(String _path, Element node) {
        String desc = null;
        if(node != null) {
            desc = node.html();
        }
        //final View childLayout = loadVideoRemote(_path, desc);
       /* CustomEditText text = childLayout.findViewById(R.id.desc);
        if(node != null) {
            componentsWrapper.getInputExtensions().applyStyles(text, node);
        }*/
    }

    public View loadVideoRemote(String _path){
        final View childLayout = ((Activity) editorCore.getContext()).getLayoutInflater().inflate(this.editorVideoLayout, null);
        YouTubePlayerView videoView = childLayout.findViewById(R.id.playerView);
     //   CustomEditText text = childLayout.findViewById(R.id.desc);

        childLayout.setTag(createVideoTag(_path));
       // text.setTag(createSubTitleTag());

       // text.setEnabled(editorCore.getRenderType() == RenderType.Editor);
        ///loadVideoUsingLib(_path, videoView);
        editorCore.getParentView().addView(childLayout);

        if(editorCore.getRenderType()== RenderType.Editor) {
            BindEvents(childLayout);
        }

        return childLayout;
    }

    private void BindEvents(final View layout) {
        final YouTubePlayerView videoView = layout.findViewById(R.id.playerView);
        final View btn_remove = layout.findViewById(R.id.btn_VideoRemove);

        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = editorCore.getParentView().indexOfChild(layout);
                editorCore.getParentView().removeView(layout);
                hideInputHint(index);
                componentsWrapper.getInputExtensions().setFocusToPrevious(index);
            }
        });

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int paddingTop = view.getPaddingTop();
                    int paddingBottom = view.getPaddingBottom();
                    int height = view.getHeight();
                    if (event.getY() < paddingTop) {
                        editorCore.___onViewTouched(0, editorCore.getParentView().indexOfChild(layout));
                    } else if (event.getY() > height - paddingBottom) {
                        editorCore.___onViewTouched(1, editorCore.getParentView().indexOfChild(layout));
                    } else {

                    }
                    return false;
                }
                return true;//hmmmm....
            }
        });
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_remove.setVisibility(View.VISIBLE);
            }
        });
        videoView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                btn_remove.setVisibility(hasFocus ? View.VISIBLE : View.GONE);
            }
        });
    }

    @Override
    public Node buildNodeFromHTML(Element element) {
        HtmlTag tag = HtmlTag.valueOf(element.tagName().toLowerCase());
        if(tag == HtmlTag.div){
            String dataTag = element.attr("data-tag");
            if (dataTag.equals("iframe")) {
                Element video = element.child(0);
               // Element descTag = element.child(1);
                String src = video.attr("src");
                loadVideo(src);
            }
        }else {
            String src = element.attr("src");
            if(element.children().size() > 0) {
               // Element descTag = element.child(1);
                loadVideo(src);
            }else{
                loadVideoRemote(src);
            }
        }
        return null;
    }

    @Override
    public void init(ComponentsWrapper componentsWrapper) {
        this.componentsWrapper = componentsWrapper;
    }

    public VideoExtensions(EditorCore editorCore){
        super(editorCore);
        this.editorCore = editorCore;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setPlayerStateChangeListener((YouTubePlayer.PlayerStateChangeListener) this);
        youTubePlayer.setPlaybackEventListener((YouTubePlayer.PlaybackEventListener) this);
        if(!b){
            youTubePlayer.cueVideo("");
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }
}
