package com.meng.mediatool.picture.pixiv;

import com.google.gson.annotations.*;
import java.util.*;

public class PaitenerAllPictures {
    public boolean error = false;
    public String message = "";
    public Body body = new Body();

	public class Body {
		public Object illusts = new Object();
		public Object manga = new Object();
		public ArrayList<Object> novels = new ArrayList<>();
		public ArrayList<Object> mangaSeries = new ArrayList<>();
		public ArrayList<Object> novelSeries = new ArrayList<>();
		public ArrayList<Pickup> pickup = new ArrayList<>();
		public BookmarkCount bookmarkCount = new BookmarkCount();


		public class Pickup {
			public String id = "";
			public String title = "";
			public int illustType = 0;
			public int xRestrict = 0;
			public int restrict = 0;
			public int sl = 0;
			public String description = "";
			public String url = "";
			public ArrayList<String> tags = new ArrayList<>();
			public String userId = "";
			public String userName = "";
			public int width = 0;
			public int height = 0;
			public int pageCount = 0;
			public boolean isBookmarkable = false;
			public Object bookmarkData = new Object();
			public String type = "";
			public boolean deletable = false;
			public boolean draggable = false;
			public String contentUrl = "";
		}

		public class BookmarkCount {
			@SerializedName("public")
			public Public _public = new Public();
			@SerializedName("private")
			public Private _private = new Private();
		}

		public class Private {
			public int illust = 0;
			public int novel = 0;
		}

		public class Public {
			public int illust = 0;
			public int novel = 0;
		}
	}
}
