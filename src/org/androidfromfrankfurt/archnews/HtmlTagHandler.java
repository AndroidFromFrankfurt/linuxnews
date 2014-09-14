package org.androidfromfrankfurt.archnews;

import java.util.Vector;
 
import org.xml.sax.XMLReader;
 
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.style.BulletSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
 
public class HtmlTagHandler implements Html.TagHandler {
private int mListItemCount = 0;
private Vector<String> mListParents = new Vector<String>();
 
@Override
public void handleTag(final boolean opening, final String tag, Editable output, final XMLReader xmlReader) {
 
if (tag.equals("ul") || tag.equals("ol") || tag.equals("dd")) {
if (opening) {
mListParents.add(tag);
} else mListParents.remove(tag);
 
mListItemCount = 0;
} else if (tag.equals("li") && !opening) {
handleListTag(output);
}
else if(tag.equalsIgnoreCase("code")) {
if(opening) {
output.setSpan(new TypefaceSpan("monospace"), output.length(), output.length(), Spannable.SPAN_MARK_MARK);
} else {
Log.d("COde Tag","Code tag encountered");
Object obj = getLast(output, TypefaceSpan.class);
int where = output.getSpanStart(obj);
output.setSpan(new TypefaceSpan("monospace"), where, output.length(), 0);
}
}
}
 
private Object getLast(Editable text, Class kind) {
Object[] objs = text.getSpans(0, text.length(), kind);
if(objs.length == 0) {
return null;
} else {
for (int i=objs.length; i > 0; i--) {
if(text.getSpanFlags(objs[i-1]) == Spannable.SPAN_MARK_MARK) {
return objs[i-1];
}
}
return null;
}
}
private void handleListTag(Editable output) {
if (mListParents.lastElement().equals("ul")) {
output.append("\n");
String[] split = output.toString().split("\n");
 
int lastIndex = split.length - 1;
int start = output.length() - split[lastIndex].length() - 1;
output.setSpan(new BulletSpan(15 * mListParents.size()), start, output.length(), 0);
} else if (mListParents.lastElement().equals("ol")) {
mListItemCount++;
 
output.append("\n");
String[] split = output.toString().split("\n");
 
int lastIndex = split.length - 1;
int start = output.length() - split[lastIndex].length() - 1;
output.insert(start, mListItemCount + ". ");
output.setSpan(new LeadingMarginSpan.Standard(15 * mListParents.size()), start, output.length(), 0);
}
}
}