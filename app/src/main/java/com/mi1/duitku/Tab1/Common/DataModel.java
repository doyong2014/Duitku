package com.mi1.duitku.Tab1.Common;

import android.os.Build;
import android.text.Html;

/**
 * Created by owner on 3/7/2017.
 */

public class DataModel {

    public String status;
    public int count;
    public int pages;
    public Category category;
    public Post posts[];

    public class Category {
        public int id;
        public String slug;
        public String title;
        public String description;
        public int parent;
        public int post_count;
    }

    public class Post {
        public int id;
        public String type;
        public String slug;
        public String url;
        public String status;
        public String title;
        public String title_plain;
        public String content;
        public String excerpt;
        public String date;
        public String modified;
        public Category categories[];
        public Tag tags[];
        public Author author;
        public Comment comments[];
        public Attachment attachments[];
        public int comment_count;
        public String comment_status;
        public String thumbnail;
        public Custom_fields custom_fields;
        public String thumbnail_size;
        public Thumbnail_images thumbnail_images;

        public String getTitle() {
            if (Build.VERSION.SDK_INT >= 24)
            {
                return Html.fromHtml(this.title, Html.FROM_HTML_MODE_LEGACY).toString();
            }
            else
            {
                return Html.fromHtml(this.title).toString();
            }
        }

        public String getContent() {
            if (Build.VERSION.SDK_INT >= 24)
            {
                return Html.fromHtml(this.title, Html.FROM_HTML_MODE_LEGACY).toString();
            }
            else
            {
                return Html.fromHtml(this.title).toString();
            }
        }
        public String getUrl() {
            if (Build.VERSION.SDK_INT >= 24)
            {
                return Html.fromHtml(this.url, Html.FROM_HTML_MODE_LEGACY).toString();
            }
            else
            {
                return Html.fromHtml(this.url).toString();
            }
        }

        public class Category {
            public int id;
            public String slug;
            public String title;
            public String description;
            public int parent;
            public int post_count;
        }

        public class Tag {
            public Tag(){
            }
        }

        public class Author {
            public int id;
            public String slug;
            public String name;
            public String first_name;
            public String last_name;
            public String nickname;
            public String url;
            public String description;
        }

        public class Comment {

            public Comment(){
            }
        }

        public class Attachment {
            public int id;
            public String url;
            public String slug;
            public String title;
            public String description;
            public String caption;
            public int parent;
            public String mime_type;
            public Images images;

            public class Images {

                public Full full;
                public Thumbnail thumbnail;
                public Medium medium;
                public Sensible_wp_home_blog sensible_wp_home_blog;

                public class Full {
                    public String url;
                    public int width;
                    public int height;
                }

                public class Thumbnail {
                    public String url;
                    public int width;
                    public int height;
                }

                public class Medium {
                    public String url;
                    public int width;
                    public int height;
                }

                public class Sensible_wp_home_blog {
                    public String url;
                    public int width;
                    public int height;
                }
            }
        }

        public class Custom_fields {

            public Custom_fields(){
            }
        }

        public class Thumbnail_images {
            public Full full;
            public Thumbnail thumbnail;
            public Medium medium;
            public Sensible_wp_home_blog sensible_wp_home_blog;

            public class Full {
                public String url;
                public int width;
                public int height;
            }

            public class Thumbnail {
                public String url;
                public int width;
                public int height;
            }

            public class Medium {
                public String url;
                public int width;
                public int height;
            }

            public class Sensible_wp_home_blog {
                public String url;
                public int width;
                public int height;
            }
        }
    }
}