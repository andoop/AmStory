package andoop.android.amstory.data;

import android.content.Context;

/**
 * Created by domob on 2017/2/15.
 */

public class JsonReportory {
    private Context context;
    private static JsonReportory INSTANCE;

    private JsonReportory(Context context) {
        this.context = context;
    }

    public static JsonReportory newInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (JsonReportory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new JsonReportory(context);
                }
            }

        }
        return INSTANCE;
    }
    public String getStory(String type, int page){

        return "{\n" +
                "    \"data\": {\n" +
                "        \"storys\": [\n" +
                "            {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"方法反反复复方法反反复复反复\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=5d728f267b8b4710da22f59ea2a7a898/a1ec08fa513d2697be3d7f5a5cfbb2fb4216d8cf.jpg\",\n" +
                "                \"title\": \"乌鸦喝水\",\n" +
                "                \"voice\": \"\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"小马过河&&&小马和他的妈妈住在绿草茵茵的十分美丽的小河边。&&&妈妈过河给河对岸的村子送粮食的时候，&&&他总是跟随在妈妈的身边寸步不离。&&&他过的很快乐，时光飞快地过去了。&&&有一天，妈妈把小马叫到身边说：&&&“小马，你已经长大了，可以帮妈妈做事了。&&&今天你把这袋粮食送到河对岸的村子里去吧。”&&&小马非常高兴地答应了。&&&他驮着粮食飞快地来到了小河边。&&&可是河上没有桥，只能自己淌过去。&&&可又不知道河水有多深呢？&&&犹豫中的小马一抬头，看见了正在不远处吃草的牛伯伯。&&&小马赶紧跑过去问到：&&&“牛伯伯，您知道那河里的水深不深呀？”&&&" +
                "牛伯伯挺起他那高大的身体笑着说：&&&“不深，不深。才到我的小腿。”&&&小马高兴地跑回" +
                "河边准备淌过河去。&&&他刚一迈腿，忽然听见一个声音说：&&&“小马，小马别下去，这河可深" +
                "啦。”&&&小马低头一看，原来是小松鼠。&&&小松鼠翘着她的漂亮的尾巴，&&&睁者圆圆的眼睛，很认真地说：&&&“前两天我的一个伙伴不小心掉进了河里，&&&河水就把他卷走了。”&&&小马一听没主意了。&&&" +
                "马妈妈老远地就看见小马低着头驮着粮食又回来了。&&&心想他一定是遇到困难了，就迎过" +
                "去问小马。&&&小马哭着把牛伯伯和小松鼠的话告诉了妈妈。&&&妈妈安慰小马说：“没关系，咱们" +
                "一起去看看吧。”&&&" +
                "小马和妈妈又一次来到河边，&&&妈妈这回让小马自己去试探一下河水有多深。&&&小马小心地" +
                "试探着，一步一步地淌过了河。&&&噢，他明白了，河水既没有牛伯伯说的那么浅，&&&也没有小松" +
                "鼠说的那么深。&&&只有自己亲自试过才知道。\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=4f87f21cc5cec3fd9f33af27b7e1bf5a/58ee3d6d55fbb2fbb58357c2494a20a44723dcb2.jpg\",\n" +
                "                \"title\": \"小马过河\",\n" +
                "                \"voice\": \"\"\n" +
                "            },{\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"事实上事实上事实上事实上事实上事实上事实上\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=b956b1f3b24543a9e116f29e7f7ee1e7/b17eca8065380cd79b6ccdeaa444ad3459828142.jpg\",\n" +
                "                \"title\": \"坐井观天\",\n" +
                "                \"voice\": \"\"\n" +
                "            }, {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"方法反反复复方法反反复复反复\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=5d728f267b8b4710da22f59ea2a7a898/a1ec08fa513d2697be3d7f5a5cfbb2fb4216d8cf.jpg\",\n" +
                "                \"title\": \"乌鸦喝水\",\n" +
                "                \"voice\": \"\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"事实上事实上事实上事实上事实上事实上事实上\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=4f87f21cc5cec3fd9f33af27b7e1bf5a/58ee3d6d55fbb2fbb58357c2494a20a44723dcb2.jpg\",\n" +
                "                \"title\": \"小马过河\",\n" +
                "                \"voice\": \"\"\n" +
                "            },{\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"事实上事实上事实上事实上事实上事实上事实上\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=b956b1f3b24543a9e116f29e7f7ee1e7/b17eca8065380cd79b6ccdeaa444ad3459828142.jpg\",\n" +
                "                \"title\": \"坐井观天\",\n" +
                "                \"voice\": \"\"\n" +
                "            }, {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"方法反反复复方法反反复复反复\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=5d728f267b8b4710da22f59ea2a7a898/a1ec08fa513d2697be3d7f5a5cfbb2fb4216d8cf.jpg\",\n" +
                "                \"title\": \"乌鸦喝水\",\n" +
                "                \"voice\": \"\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"事实上事实上事实上事实上事实上事实上事实上\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=4f87f21cc5cec3fd9f33af27b7e1bf5a/58ee3d6d55fbb2fbb58357c2494a20a44723dcb2.jpg\",\n" +
                "                \"title\": \"小马过河\",\n" +
                "                \"voice\": \"\"\n" +
                "            },{\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"事实上事实上事实上事实上事实上事实上事实上\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=b956b1f3b24543a9e116f29e7f7ee1e7/b17eca8065380cd79b6ccdeaa444ad3459828142.jpg\",\n" +
                "                \"title\": \"坐井观天\",\n" +
                "                \"voice\": \"\"\n" +
                "            }, {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"方法反反复复方法反反复复反复\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=5d728f267b8b4710da22f59ea2a7a898/a1ec08fa513d2697be3d7f5a5cfbb2fb4216d8cf.jpg\",\n" +
                "                \"title\": \"乌鸦喝水\",\n" +
                "                \"voice\": \"\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"事实上事实上事实上事实上事实上事实上事实上\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=4f87f21cc5cec3fd9f33af27b7e1bf5a/58ee3d6d55fbb2fbb58357c2494a20a44723dcb2.jpg\",\n" +
                "                \"title\": \"小马过河\",\n" +
                "                \"voice\": \"\"\n" +
                "            },{\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"事实上事实上事实上事实上事实上事实上事实上\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=b956b1f3b24543a9e116f29e7f7ee1e7/b17eca8065380cd79b6ccdeaa444ad3459828142.jpg\",\n" +
                "                \"title\": \"坐井观天\",\n" +
                "                \"voice\": \"\"\n" +
                "            }, {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"方法反反复复方法反反复复反复\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=5d728f267b8b4710da22f59ea2a7a898/a1ec08fa513d2697be3d7f5a5cfbb2fb4216d8cf.jpg\",\n" +
                "                \"title\": \"乌鸦喝水\",\n" +
                "                \"voice\": \"\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"事实上事实上事实上事实上事实上事实上事实上\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=4f87f21cc5cec3fd9f33af27b7e1bf5a/58ee3d6d55fbb2fbb58357c2494a20a44723dcb2.jpg\",\n" +
                "                \"title\": \"小马过河\",\n" +
                "                \"voice\": \"\"\n" +
                "            },{\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"事实上事实上事实上事实上事实上事实上事实上\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=b956b1f3b24543a9e116f29e7f7ee1e7/b17eca8065380cd79b6ccdeaa444ad3459828142.jpg\",\n" +
                "                \"title\": \"坐井观天\",\n" +
                "                \"voice\": \"\"\n" +
                "            }, {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"方法反反复复方法反反复复反复\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=5d728f267b8b4710da22f59ea2a7a898/a1ec08fa513d2697be3d7f5a5cfbb2fb4216d8cf.jpg\",\n" +
                "                \"title\": \"乌鸦喝水\",\n" +
                "                \"voice\": \"\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"事实上事实上事实上事实上事实上事实上事实上\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=4f87f21cc5cec3fd9f33af27b7e1bf5a/58ee3d6d55fbb2fbb58357c2494a20a44723dcb2.jpg\",\n" +
                "                \"title\": \"小马过河\",\n" +
                "                \"voice\": \"\"\n" +
                "            },{\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"事实上事实上事实上事实上事实上事实上事实上\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=b956b1f3b24543a9e116f29e7f7ee1e7/b17eca8065380cd79b6ccdeaa444ad3459828142.jpg\",\n" +
                "                \"title\": \"坐井观天\",\n" +
                "                \"voice\": \"\"\n" +
                "            }, {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"方法反反复复方法反反复复反复\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=5d728f267b8b4710da22f59ea2a7a898/a1ec08fa513d2697be3d7f5a5cfbb2fb4216d8cf.jpg\",\n" +
                "                \"title\": \"乌鸦喝水\",\n" +
                "                \"voice\": \"\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"事实上事实上事实上事实上事实上事实上事实上\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=4f87f21cc5cec3fd9f33af27b7e1bf5a/58ee3d6d55fbb2fbb58357c2494a20a44723dcb2.jpg\",\n" +
                "                \"title\": \"小马过河\",\n" +
                "                \"voice\": \"\"\n" +
                "            },{\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"事实上事实上事实上事实上事实上事实上事实上\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"https://imgsa.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=b956b1f3b24543a9e116f29e7f7ee1e7/b17eca8065380cd79b6ccdeaa444ad3459828142.jpg\",\n" +
                "                \"title\": \"坐井观天\",\n" +
                "                \"voice\": \"\"\n" +
                "            }\n" +
                "        ],\n" +
                "        \"type\": 1\n" +
                "    },\n" +
                "    \"error\": {\n" +
                "        \"code\": 200,\n" +
                "        \"msg\": \"\"\n" +
                "    }\n" +
                "}";
    }

    public String getBanner(){

        return "{\n" +
                "    \"data\": {\n" +
                "        \"banner\": [\n" +
                "            {\n" +
                "                \"desc\": \"banner01\",\n" +
                "                \"img\": \"http://img.dahe.cn/qf/2017/2/16/920U0RFP3.jpg\",\n" +
                "                \"url\": \"http://news.dahe.cn/2017/02-16/108287701.html\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"desc\": \"banner02\",\n" +
                "                \"img\": \"http://img.mp.itc.cn/upload/20160711/1eec435d6b5e41a884131076b297c73b_th.jpg\",\n" +
                "                \"url\": \"http://chanye.07073.com/guonei/1553341.html\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"desc\": \"banner03\",\n" +
                "                \"img\": \"http://images.enet.com.cn/i/2017/0216/103424869.jpg\",\n" +
                "                \"url\": \"http://games.enet.com.cn/a/2017/0216/A2017021600604712_0.shtml\"\n" +
                "            }\n" +
                "        ]\n" +
                "    },\n" +
                "    \"error\": {\n" +
                "        \"code\": 200,\n" +
                "        \"msg\": \"\"\n" +
                "    }\n" +
                "}";
    }

}
