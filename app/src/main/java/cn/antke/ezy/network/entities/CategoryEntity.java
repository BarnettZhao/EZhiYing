package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zww on 2017/6/20.
 */

public class CategoryEntity {
    @SerializedName("category_list")
    private List<CategoryItemEntity> categoryItemEntities;
    @SerializedName("pic_list")
    private List<BannerEntity> bannerEntities;

    public List<CategoryItemEntity> getCategoryItemEntities() {
        return categoryItemEntities;
    }

    public void setCategoryItemEntities(List<CategoryItemEntity> categoryItemEntities) {
        this.categoryItemEntities = categoryItemEntities;
    }

    public List<BannerEntity> getBannerEntities() {
        return bannerEntities;
    }

    public void setBannerEntities(List<BannerEntity> bannerEntities) {
        this.bannerEntities = bannerEntities;
    }
}
