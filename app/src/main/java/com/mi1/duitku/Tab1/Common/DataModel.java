package com.mi1.duitku.Tab1.Common;

/**
 * Created by owner on 3/7/2017.
 */

public class DataModel {

    public String status;
    public int count;
    public int pages;
    public Category category;
    public Post posts[];

    public DataModel(String status, int count, int pages, Category category, Post[] posts){
        this.status = status;
        this.count = count;
        this.pages = pages;
        this.category = category;
        this.posts = posts;
    }

    public class Category {
        public int id;
        public String slug;
        public String title;
        public String description;
        public int parent;
        public int post_count;

        public Category(int id, String slug, String title, String description, int parent, int post_count){
            this.id = id;
            this.slug = slug;
            this.title = title;
            this.description = description;
            this.parent = parent;
            this.post_count = post_count;
        }
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

        public Post(int id, String type, String slug, String url, String status, String title, String title_plain, String content, String excerpt, String date, String modified, Category[] categories, Tag[] tags, Author author, Comment[] comments, Attachment[] attachments, int comment_count, String comment_status, String thumbnail, Custom_fields custom_fields, String thumbnail_size, Thumbnail_images thumbnail_images){
            this.id = id;
            this.type = type;
            this.slug = slug;
            this.url = url;
            this.status = status;
            this.title = title;
            this.title_plain = title_plain;
            this.content = content;
            this.excerpt = excerpt;
            this.date = date;
            this.modified = modified;
            this.categories = categories;
            this.tags = tags;
            this.author = author;
            this.comments = comments;
            this.attachments = attachments;
            this.comment_count = comment_count;
            this.comment_status = comment_status;
            this.thumbnail = thumbnail;
            this.custom_fields = custom_fields;
            this.thumbnail_size = thumbnail_size;
            this.thumbnail_images = thumbnail_images;
        }

        public class Category {
            public int id;
            public String slug;
            public String title;
            public String description;
            public int parent;
            public int post_count;

            public Category(int id, String slug, String title, String description, int parent, int post_count){
                this.id = id;
                this.slug = slug;
                this.title = title;
                this.description = description;
                this.parent = parent;
                this.post_count = post_count;
            }
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

            public Author(int id, String slug, String name, String first_name, String last_name, String nickname,String url, String description){
                this.id = id;
                this.slug = slug;
                this.name = name;
                this.first_name = first_name;
                this.last_name = last_name;
                this.nickname = nickname;
                this.url = url;
                this.description = description;
            }
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

            public Attachment(int id, String url, String slug, String title, String description, String caption, int parent, String mime_type, Images images){
                this.id = id;
                this.url = url;
                this.slug = slug;
                this.title = title;
                this.description = description;
                this.caption = caption;
                this.parent = parent;
                this.mime_type = mime_type;
                this.images = images;
            }

            public class Images {
                public Full full;
                public Thumbnail thumbnail;
                public Medium medium;
                public Sensible_wp_home_blog sensible_wp_home_blog;

                public Images(Full full, Thumbnail thumbnail, Medium medium, Sensible_wp_home_blog sensible_wp_home_blog){
                    this.full = full;
                    this.thumbnail = thumbnail;
                    this.medium = medium;
                    this.sensible_wp_home_blog = sensible_wp_home_blog;
                }

                public class Full {
                    public String url;
                    public int width;
                    public int height;

                    public Full(String url, int width, int height){
                        this.url = url;
                        this.width = width;
                        this.height = height;
                    }
                }

                public class Thumbnail {
                    public String url;
                    public int width;
                    public int height;

                    public Thumbnail(String url, int width, int height){
                        this.url = url;
                        this.width = width;
                        this.height = height;
                    }
                }

                public class Medium {
                    public String url;
                    public int width;
                    public int height;

                    public Medium(String url, int width, int height){
                        this.url = url;
                        this.width = width;
                        this.height = height;
                    }
                }

                public class Sensible_wp_home_blog {
                    public String url;
                    public int width;
                    public int height;

                    public Sensible_wp_home_blog(String url, int width, int height){
                        this.url = url;
                        this.width = width;
                        this.height = height;
                    }
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

            public Thumbnail_images(Full full, Thumbnail thumbnail, Medium medium, Sensible_wp_home_blog sensible_wp_home_blog){
                this.full = full;
                this.thumbnail = thumbnail;
                this.medium = medium;
                this.sensible_wp_home_blog = sensible_wp_home_blog;
            }

            public class Full {
                public String url;
                public int width;
                public int height;

                public Full(String url, int width, int height){
                    this.url = url;
                    this.width = width;
                    this.height = height;
                }
            }

            public class Thumbnail {
                public String url;
                public int width;
                public int height;

                public Thumbnail(String url, int width, int height){
                    this.url = url;
                    this.width = width;
                    this.height = height;
                }
            }

            public class Medium {
                public String url;
                public int width;
                public int height;

                public Medium(String url, int width, int height){
                    this.url = url;
                    this.width = width;
                    this.height = height;
                }
            }

            public class Sensible_wp_home_blog {
                public String url;
                public int width;
                public int height;

                public Sensible_wp_home_blog(String url, int width, int height){
                    this.url = url;
                    this.width = width;
                    this.height = height;
                }
            }
        }
    }
}