package com.example.foodcalu.data.db;

import com.example.foodcalu.data.entity.WorkoutCategory;
import com.example.foodcalu.data.entity.WorkoutItem;

import java.util.Arrays;
import java.util.List;

public final class DataSeeder {
    private DataSeeder() {
    }

    public static void seed(AppDatabase db) {
        if (db == null) {
            return;
        }

        if (db.workoutCategoryDao().getCount() == 0) {
            List<WorkoutCategory> categories = Arrays.asList(
                    new WorkoutCategory(1, "徒手基础", "基础力量与稳定性训练。"),
                    new WorkoutCategory(2, "有氧燃脂", "提升心率与燃脂训练。"),
                    new WorkoutCategory(3, "拉伸恢复", "柔韧与放松恢复训练。")
            );
            db.workoutCategoryDao().insertAll(categories);
        }

        if (db.workoutItemDao().getCount() == 0) {
            List<WorkoutItem> items = Arrays.asList(
                    new WorkoutItem(0, 1, "俯卧撑", "简单", "胸部/肱三头肌", "3组 x 12次", 80,
                            "https://cn-jsnt-ct-01-02.bilivideo.com/upgcxcode/14/80/424908014/424908014-1-208.mp4?e=ig8euxZM2rNcNbRahWdVhwdlhWu1hwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&trid=00005d6f6344d8bf4ca3a867fe2833f3634T&deadline=1767104261&gen=playurlv3&og=cos&mid=297763360&nbs=1&oi=1696788563&os=bcache&uipk=5&platform=html5&upsig=c2d9d6d99d0aece4e616c0bca8d2336e&uparams=e,trid,deadline,gen,og,mid,nbs,oi,os,uipk,platform&cdnid=4274&bvc=vod&nettype=0&bw=1369117&mobi_app=&agrr=0&buvid=&build=0&dl=0&f=T_0_0&orderid=0,1", null),
                    new WorkoutItem(0, 1, "深蹲", "简单", "腿部/臀部", "3组 x 15次", 90,
                            "https://cn-hbyc-ct-01-02.bilivideo.com/upgcxcode/55/84/824678455/824678455-1-208.mp4?e=ig8euxZM2rNcNbRj7WdVhwdlhWTBhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&deadline=1767104591&platform=html5&gen=playurlv3&oi=1696788563&trid=0000df03d4a76fc24920bd36a97dfaf5a5bT&os=bcache&og=hw&nbs=1&uipk=5&mid=48841537&upsig=2c86c45922600517be1c70b674dc94f2&uparams=e,deadline,platform,gen,oi,trid,os,og,nbs,uipk,mid&cdnid=88802&bvc=vod&nettype=0&bw=1324967&f=T_0_0&mobi_app=&agrr=0&buvid=&build=0&dl=0&orderid=0,1", null),
                    new WorkoutItem(0, 1, "平板支撑", "中等", "核心", "3组 x 45秒", 60,
                            "https://cn-hljheb-ct-01-03.bilivideo.com/upgcxcode/45/18/33637271845/33637271845-1-192.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&nbs=1&gen=playurlv3&os=bcache&mid=4676085&uipk=5&platform=html5&trid=000038d0cd6a652b45af8a8f6f21e2bc3e8T&oi=1696788563&deadline=1767104605&og=hw&upsig=27d386f0e8cd47baaaca4b749a5fd0db&uparams=e,nbs,gen,os,mid,uipk,platform,trid,oi,deadline,og&cdnid=3842&bvc=vod&nettype=0&bw=373735&build=0&dl=0&f=T_0_0&mobi_app=&agrr=0&buvid=&orderid=0,1", null),
                    new WorkoutItem(0, 1, "弓步蹲", "中等", "腿部/平衡", "3组 x 10次", 85,
                            "https://cn-hljheb-ct-01-03.bilivideo.com/upgcxcode/53/26/1578402653/1578402653-1-192.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&mid=7873971&deadline=1767104658&trid=0000af697dc7203f404aa640a4f481ab33eT&oi=1696788563&gen=playurlv3&uipk=5&nbs=1&platform=html5&os=bcache&og=cos&upsig=735e8b25fdea0a4cde54e58a7bfc2a95&uparams=e,mid,deadline,trid,oi,gen,uipk,nbs,platform,os,og&cdnid=3842&bvc=vod&nettype=0&bw=353433&buvid=&build=0&dl=0&f=T_0_0&mobi_app=&agrr=0&orderid=0,1", null),
                    new WorkoutItem(0, 2, "慢跑", "简单", "有氧", "20分钟匀速", 200,
                            "https://cn-lndl-ct-01-01.bilivideo.com/upgcxcode/82/56/33893515682/33893515682-1-192.mp4?e=ig8euxZM2rNcNbNghbdVhwdlhbNghwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&trid=0000c6f4768b21684239b66c3e385fdcfb1T&deadline=1767104706&uipk=5&mid=523601&os=bcache&nbs=1&platform=html5&oi=1696788563&gen=playurlv3&og=cos&upsig=71c43c396f6657d5c7aeba570000a21b&uparams=e,trid,deadline,uipk,mid,os,nbs,platform,oi,gen,og&cdnid=88001&bvc=vod&nettype=0&bw=1782770&dl=0&f=T_0_0&mobi_app=&agrr=0&buvid=&build=0&orderid=0,1", null),
                    new WorkoutItem(0, 2, "跳绳", "中等", "有氧", "10分钟间歇", 180,
                            "https://cn-jsnt-ct-01-11.bilivideo.com/upgcxcode/34/37/380063734/380063734_da2-1-192.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&oi=1696788563&mid=23516165&nbs=1&platform=html5&gen=playurlv3&os=bcache&deadline=1767104764&uipk=5&trid=00009fe91a005ce54fb084802ea0825f92bT&og=ali&upsig=49cb05b6ac9067511f584d70d17c5e74&uparams=e,oi,mid,nbs,platform,gen,os,deadline,uipk,trid,og&cdnid=4281&bvc=vod&nettype=0&bw=554397&build=0&dl=0&f=T_0_0&mobi_app=&agrr=0&buvid=&orderid=0,1", null),
                    new WorkoutItem(0, 2, "骑行", "简单", "有氧", "25分钟匀速", 220,
                            "https://cn-zjjh-ct-04-03.bilivideo.com/upgcxcode/15/16/1112111615/1112111615-1-208.mp4?e=ig8euxZM2rNcNbh1hbdVhwdlhzRghwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&trid=0000486e61396c0c4d29a9642a936ac001aT&os=bcache&nbs=1&platform=html5&gen=playurlv3&og=ali&mid=355027432&deadline=1767104816&uipk=5&oi=1696788563&upsig=c27de4813b572fcacd9ff6008cb82bc0&uparams=e,trid,os,nbs,platform,gen,og,mid,deadline,uipk,oi&cdnid=6588&bvc=vod&nettype=0&bw=2502389&mobi_app=&agrr=0&buvid=&build=0&dl=0&f=T_0_0&orderid=0,1", null),
                    new WorkoutItem(0, 2, "波比跳", "困难", "全身", "3组 x 8次", 150,
                            "https://cn-zjjh-ct-04-06.bilivideo.com/upgcxcode/40/67/158646740/158646740-1-208.mp4?e=ig8euxZM2rNcNbRa7wdVhwdlhWuMhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&gen=playurlv3&os=bcache&uipk=5&platform=html5&nbs=1&trid=0000c1d9d178fe9a49cd9724e882bf64f34T&og=cos&deadline=1767104922&oi=1696788563&mid=19201160&upsig=7406c27d85b7edd3fa9a00e5d18d2faa&uparams=e,gen,os,uipk,platform,nbs,trid,og,deadline,oi,mid&cdnid=6591&bvc=vod&nettype=0&bw=1399981&dl=0&f=T_0_0&mobi_app=&agrr=0&buvid=&build=0&orderid=0,1", null),
                    new WorkoutItem(0, 3, "颈部拉伸", "简单", "颈部", "2分钟", 20,
                            "https://cn-zjjh-ct-04-03.bilivideo.com/upgcxcode/76/19/444921976/444921976_u1-1-208.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&trid=0000270f99b72a8a4d5fa006eb8af13d943T&deadline=1767104941&oi=1696788563&gen=playurlv3&os=bcache&og=hw&nbs=1&mid=12536964&uipk=5&platform=html5&upsig=f91bb06dcb1418cf7be2a9be7e855b46&uparams=e,trid,deadline,oi,gen,os,og,nbs,mid,uipk,platform&cdnid=6588&bvc=vod&nettype=0&bw=808540&f=T_0_0&mobi_app=&agrr=0&buvid=&build=0&dl=0&orderid=0,1", null),
                    new WorkoutItem(0, 3, "髋部拉伸", "简单", "髋部", "3分钟", 25,
                            "https://upos-sz-mirror08c.bilivideo.com/upgcxcode/17/26/999042617/999042617-1-192.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&platform=html5&deadline=1767104978&gen=playurlv3&oi=1696788563&uipk=5&trid=3c6898e899cd419185324ba484bb3a8T&mid=34769480&os=08cbv&og=hw&nbs=1&upsig=026acf0b06181ffd3062650b2d65bd98&uparams=e,platform,deadline,gen,oi,uipk,trid,mid,os,og,nbs&bvc=vod&nettype=0&bw=541316&buvid=&build=0&dl=0&f=T_0_0&mobi_app=&agrr=0&orderid=0,1", null),
                    new WorkoutItem(0, 3, "腘绳肌拉伸", "简单", "腿部", "3分钟", 25,
                            "https://cn-zjjh-ct-04-05.bilivideo.com/upgcxcode/75/57/34977025775/34977025775-1-192.mp4?e=ig8euxZM2rNcNbRVhwdVhwdlhWdVhwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&gen=playurlv3&os=bcache&mid=12380835&platform=html5&deadline=1767105010&nbs=1&uipk=5&oi=1696788563&og=cos&trid=000060a2d35e806a41119afded244247369T&upsig=a62b21088e6f7dd269ba14378ffe6de0&uparams=e,gen,os,mid,platform,deadline,nbs,uipk,oi,og,trid&cdnid=6590&bvc=vod&nettype=0&bw=275314&agrr=0&buvid=&build=0&dl=0&f=T_0_0&mobi_app=&orderid=0,1", null),
                    new WorkoutItem(0, 3, "肩部拉伸", "简单", "肩部", "3分钟", 25,
                            "https://cn-jsnt-ct-01-01.bilivideo.com/upgcxcode/39/37/18583739/18583739-1-48.mp4?e=ig8euxZM2rNcNbR17zdVhwdlhWRahwdVhoNvNC8BqJIzNbfq9rVEuxTEnE8L5F6VnEsSTx0vkX8fqJeYTj_lta53NCM=&trid=0000ed27ddcec14147708264bfea22a1ef5T&deadline=1767105122&uipk=5&mid=483861285&os=bcache&og=hw&platform=html5&oi=1696788563&nbs=1&gen=playurlv3&upsig=405811b6a76a3927d02b7c9629400929&uparams=e,trid,deadline,uipk,mid,os,og,platform,oi,nbs,gen&cdnid=4309&bvc=vod&nettype=0&bw=940972&build=0&dl=0&f=T_0_0&mobi_app=&agrr=0&buvid=&orderid=0,1", null)
            );
            db.workoutItemDao().insertAll(items);
        }
    }
}
