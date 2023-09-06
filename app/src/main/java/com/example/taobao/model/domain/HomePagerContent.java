package com.example.taobao.model.domain;

import java.util.List;



public class HomePagerContent {


    @Override
    public String toString() {
        return "HomePagerContent{" +
                "success=" + success +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }




    private boolean success;
    private long code;
    private String message;
    private List<DataBean> data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements ILinearItemInfo {
        @Override
        public String toString() {
            return "DataBean{" +
                    "category_id=" + category_id +
                    ", click_url='" + click_url + '\'' +
                    ", commission_rate='" + commission_rate + '\'' +
                    ", coupon_amount=" + coupon_amount +
                    ", coupon_click_url='" + coupon_click_url + '\'' +
                    ", coupon_end_time='" + coupon_end_time + '\'' +
                    ", coupon_remain_count=" + coupon_remain_count +
                    ", coupon_share_url='" + coupon_share_url + '\'' +
                    ", coupon_start_fee='" + coupon_start_fee + '\'' +
                    ", coupon_start_time='" + coupon_start_time + '\'' +
                    ", coupon_total_count=" + coupon_total_count +
                    ", item_description='" + item_description + '\'' +
                    ", item_id='" + item_id + '\'' +
                    ", level_one_category_id=" + level_one_category_id +
                    ", level_one_category_name='" + level_one_category_name + '\'' +
                    ", pict_url='" + pict_url + '\'' +
                    ", reserve_price='" + reserve_price + '\'' +
                    ", seller_id=" + seller_id +
                    ", short_title=" + short_title +
                    ", small_images=" + small_images +
                    ", sub_title='" + sub_title + '\'' +
                    ", title='" + title + '\'' +
                    ", tmall_play_activity_end_time=" + tmall_play_activity_end_time +
                    ", tmall_play_activity_start_time=" + tmall_play_activity_start_time +
                    ", user_type=" + user_type +
                    ", volume=" + volume +
                    ", zk_final_price='" + zk_final_price + '\'' +
                    '}';
        }



        private long category_id;
        private String click_url;
        private String commission_rate;
        private long coupon_amount;
        private String coupon_click_url;
        private String coupon_end_time;
        private long coupon_remain_count;
        private String coupon_share_url;
        private String coupon_start_fee;
        private String coupon_start_time;
        private long coupon_total_count;
        private String item_description;
        private String item_id;
        private long level_one_category_id;
        private String level_one_category_name;
        private String pict_url;
        private String reserve_price;
        private long seller_id;
        private Object short_title;
        private SmallImagesBean small_images;
        private String sub_title;
        private String title;
        private long tmall_play_activity_end_time;
        private long tmall_play_activity_start_time;
        private long user_type;
        private long volume;
        private String zk_final_price;

        public long getCategory_id() {
            return category_id;
        }

        public void setCategory_id(long category_id) {
            this.category_id = category_id;
        }

        public String getClick_url() {
            return click_url;
        }

        public void setClick_url(String click_url) {
            this.click_url = click_url;
        }

        public String getCommission_rate() {
            return commission_rate;
        }

        public void setCommission_rate(String commission_rate) {
            this.commission_rate = commission_rate;
        }

        public long getCoupon_amount() {
            return coupon_amount;
        }

        public void setCoupon_amount(long coupon_amount) {
            this.coupon_amount = coupon_amount;
        }

        public String getCoupon_click_url() {
            return coupon_click_url;
        }

        public void setCoupon_click_url(String coupon_click_url) {
            this.coupon_click_url = coupon_click_url;
        }

        public String getCoupon_end_time() {
            return coupon_end_time;
        }

        public void setCoupon_end_time(String coupon_end_time) {
            this.coupon_end_time = coupon_end_time;
        }

        public long getCoupon_remain_count() {
            return coupon_remain_count;
        }

        public void setCoupon_remain_count(long coupon_remain_count) {
            this.coupon_remain_count = coupon_remain_count;
        }

        public String getCoupon_share_url() {
            return coupon_share_url;
        }

        public void setCoupon_share_url(String coupon_share_url) {
            this.coupon_share_url = coupon_share_url;
        }

        public String getCoupon_start_fee() {
            return coupon_start_fee;
        }

        public void setCoupon_start_fee(String coupon_start_fee) {
            this.coupon_start_fee = coupon_start_fee;
        }

        public String getCoupon_start_time() {
            return coupon_start_time;
        }

        public void setCoupon_start_time(String coupon_start_time) {
            this.coupon_start_time = coupon_start_time;
        }

        public long getCoupon_total_count() {
            return coupon_total_count;
        }

        public void setCoupon_total_count(long coupon_total_count) {
            this.coupon_total_count = coupon_total_count;
        }

        public String getItem_description() {
            return item_description;
        }

        public void setItem_description(String item_description) {
            this.item_description = item_description;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public long getLevel_one_category_id() {
            return level_one_category_id;
        }

        public void setLevel_one_category_id(long level_one_category_id) {
            this.level_one_category_id = level_one_category_id;
        }

        public String getLevel_one_category_name() {
            return level_one_category_name;
        }

        public void setLevel_one_category_name(String level_one_category_name) {
            this.level_one_category_name = level_one_category_name;
        }

        public String getPict_url() {
            return pict_url;
        }

        public void setPict_url(String pict_url) {
            this.pict_url = pict_url;
        }

        public String getReserve_price() {
            return reserve_price;
        }

        public void setReserve_price(String reserve_price) {
            this.reserve_price = reserve_price;
        }

        public long getSeller_id() {
            return seller_id;
        }

        public void setSeller_id(long seller_id) {
            this.seller_id = seller_id;
        }

        public Object getShort_title() {
            return short_title;
        }

        public void setShort_title(Object short_title) {
            this.short_title = short_title;
        }

        public SmallImagesBean getSmall_images() {
            return small_images;
        }

        public void setSmall_images(SmallImagesBean small_images) {
            this.small_images = small_images;
        }

        public String getSub_title() {
            return sub_title;
        }

        public void setSub_title(String sub_title) {
            this.sub_title = sub_title;
        }

        @Override
        public String getCover() {
            return pict_url;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String getUrl() {
            return coupon_click_url==null?click_url:coupon_click_url;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getTmall_play_activity_end_time() {
            return tmall_play_activity_end_time;
        }

        public void setTmall_play_activity_end_time(long tmall_play_activity_end_time) {
            this.tmall_play_activity_end_time = tmall_play_activity_end_time;
        }

        public long getTmall_play_activity_start_time() {
            return tmall_play_activity_start_time;
        }

        public void setTmall_play_activity_start_time(long tmall_play_activity_start_time) {
            this.tmall_play_activity_start_time = tmall_play_activity_start_time;
        }

        public long getUser_type() {
            return user_type;
        }

        public void setUser_type(long user_type) {
            this.user_type = user_type;
        }

        @Override
        public String getFinalPrise() {
            return zk_final_price;
        }

        @Override
        public long getCouponAmount() {
            return coupon_amount;
        }

        public long getVolume() {
            return volume;
        }

        public void setVolume(long volume) {
            this.volume = volume;
        }

        public String getZk_final_price() {
            return zk_final_price;
        }

        public void setZk_final_price(String zk_final_price) {
            this.zk_final_price = zk_final_price;
        }

        public static class SmallImagesBean {
            @Override
            public String toString() {
                return "SmallImagesBean{" +
                        "string=" + string +
                        '}';
            }

            private List<String> string;

            public List<String> getString() {
                return string;
            }

            public void setString(List<String> string) {
                this.string = string;
            }
        }
    }
}
