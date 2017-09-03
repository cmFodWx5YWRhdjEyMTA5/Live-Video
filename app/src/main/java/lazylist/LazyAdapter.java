package lazylist;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.live.FreeVideo.R;
import com.live.FreeVideo.Session;
import java.util.ArrayList;


public class LazyAdapter extends BaseAdapter {

	private static LayoutInflater inflater = null;
	private final Context mcontext;
	public ImageLoader imageLoader;
	public ArrayList<Session.MyMovie> movies;

	private String youtube;

	public LazyAdapter(Context context, ArrayList<Session.MyMovie> movies) {
		mcontext = context;
		this.movies = movies;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(mcontext);

	}

	public int getCount() {
		return movies.toArray().length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;

		Session.MyMovie movie = movies.get(position);

		if (convertView == null)
			vi = inflater.inflate(R.layout.item, null);

		TextView text = (TextView) vi.findViewById(R.id.text);
		ImageView image = (ImageView) vi.findViewById(R.id.image);

		text.setTextColor(Color.DKGRAY);
		text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		text.setGravity(Gravity.START);

		String s = "<h3>" + movie.getUsername() + "</h3><span>" + movie.getReg_date() + "</span>";
		text.setText(Html.fromHtml(s));

		imageLoader.DisplayImage(movie.getImageName(), image);

		return vi;
	}

}