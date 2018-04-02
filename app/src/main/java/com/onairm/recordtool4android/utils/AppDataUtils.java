package com.onairm.recordtool4android.utils;

import com.onairm.recordtool4android.R;
import com.onairm.recordtool4android.bean.CategoryDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 18/1/25.
 */

public class AppDataUtils {


    public static List<CategoryDto>  getData() {
        List<CategoryDto> categoryDtoList=new ArrayList<>();
        categoryDtoList.clear();

        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setcName("爱奇艺");
        categoryDto.setLanchUrl("com.qiyi.video");
        categoryDto.setAppIc(R.mipmap.aiqiyi);
        categoryDtoList.add(categoryDto);

        CategoryDto categoryDto1 = new CategoryDto();
        categoryDto1.setcName("优酷");
        categoryDto1.setLanchUrl("com.youku.phone");
        categoryDto1.setAppIc(R.mipmap.youku);
        categoryDtoList.add(categoryDto1);


        CategoryDto categoryDto2 = new CategoryDto();
        categoryDto2.setcName("西瓜视频");
        categoryDto2.setLanchUrl("com.ss.android.article.video");
        categoryDto2.setAppIc(R.mipmap.xiguan);
        categoryDtoList.add(categoryDto2);


        CategoryDto categoryDto3 = new CategoryDto();
        categoryDto3.setcName("腾讯视频");
        categoryDto3.setLanchUrl("com.tencent.qqlive");
        categoryDto3.setAppIc(R.mipmap.tecent);
        categoryDtoList.add(categoryDto3);

        CategoryDto categoryDto4 = new CategoryDto();
        categoryDto4.setcName("斗鱼");
        categoryDto4.setLanchUrl("air.tv.douyu.android");
        categoryDto4.setAppIc(R.mipmap.douyu);
        categoryDtoList.add(categoryDto4);

        CategoryDto categoryDto5 = new CategoryDto();
        categoryDto5.setcName("快手");
        categoryDto5.setLanchUrl("com.smile.gifmaker");
        categoryDto5.setAppIc(R.mipmap.kuaishou);
        categoryDtoList.add(categoryDto5);

        CategoryDto categoryDto6 = new CategoryDto();
        categoryDto6.setcName("美拍");
        categoryDto6.setLanchUrl("com.meitu.meipaimv");
        categoryDto6.setAppIc(R.mipmap.miaopai);
        categoryDtoList.add(categoryDto6);

        return categoryDtoList;
    }
}
