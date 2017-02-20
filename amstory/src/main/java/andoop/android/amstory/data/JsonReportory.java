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
                "                \"content\": \"事实上&&&事实上&&&事实上&&&事实上&&&事实上&&&事实上&&&事实上事实上&&&事实上&&&事实上&&&事实上&&&事实上&&&事实上&&&事实上事实上&&&事实上&&&事实上&&&事实上&&&事实上&&&事实上&&&事实上事实上&&&事实上&&&事实上&&&事实上&&&事实上&&&事实上&&&事实上事实上&&&事实上&&&事实上&&&事实上&&&事实上&&&事实上&&&事实上事实上&&&事实上&&&事实上&&&事实上&&&事实上&&&事实上&&&事实上\",\n" +
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
