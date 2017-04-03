package andoop.android.amstory.data;

import android.content.Context;
import android.widget.Switch;

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
    public String getStory(int type, int page){


        String result=null;

        switch (type){
            case 1://今日推荐
                result=getDataType1();
                break;
            case 2://睡前故事
                result=getDataType2();
                break;
            case 3://健康教育
                result=getDataType3();
                break;
            case 4://品格教育
                break;
            case 5://经典诗文
                break;
            //从200开始是其他类型
            case DataManager.TYPE_TUIJIAN://获取推荐数据
                result=getDataType200();
                break;
            case DataManager.TYPE_FAXIAN://获取发现数据
                result=getDataType200();
                break;

        }

        return result;


    }

    private String getDataType3(){
                return "{\n" +
                "    \"data\": {\n" +
                "        \"storys\": [\n" +
                "            {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"天天刷牙习惯好&&&今天的午饭真丰盛，有花生、瓜子、薯片，&&&还有蜂蜜等许许多多好吃的食物。&&&小熊可高兴了，吃了这样吃那样，&&&把小肚子都撑得圆滚滚的。&&&吃饱后，小熊满足地说：&&& “妈妈，我吃饱了，我要去玩了。”&&& “等等，先漱漱口再去吧！” &&&妈妈提醒说。&&&小熊没听到，一溜烟就不见了。&&&小熊并不知道，&&&在自己的嘴巴里、牙齿上、牙缝（fènɡ）里&&&还遗留着许多食物残渣。&&&食物残渣散发出阵阵香味，引来许多牙菌。&&& “这里的食物真好吃！”&&& “这里是快乐的家园！”&&& “我要把家安在这里！”&&&牙菌们欢快地叫喊着，&&&在小熊的嘴巴里安了家。&&&到后来，小熊的整张嘴巴都快被牙菌占领了。&&&可小熊只顾着玩，&&&根本不知道嘴巴里发生的事情。&&&到了晚上，小熊回到家里。&&&这时他才觉得嘴巴里酸酸臭臭的，有点难受。&&& “张开嘴让妈妈看看！”妈妈说。&&& “啊——”&&&小熊一张嘴，就涌出一股臭味，&&&差点把妈妈熏倒了。&&&妈妈屏着气，&&&认真地检查起小熊的嘴巴。&&&她发现小熊的牙齿有点黄，&&&牙缝里还塞着许多污垢，&&&就赶紧说：“是牙菌在作怪。&&&你呀，快去刷牙，把它们赶走！”&&&小熊连忙来到卫生间刷牙。&&&他先在牙刷上抹上牙膏，&&&然后把牙刷伸进嘴巴里，&&&刷了起来。&&& “不好，小主人在刷牙了。&&&我们快躲起来。” &&&牙菌们吓坏了，&&&连滚带爬地躲到牙齿的后面和牙缝里。&&&小熊才不会放过它们呢。&&&他举着牙刷上上下下、&&&左左右右、&&&里里外外&&&把两排牙齿上的牙菌全刷了个遍。&&& “这下惨了……”&&&牙菌们再也神气不起来了，&&&接二连三地滚到口腔里。&&& “咕噜噜……”小熊含了一口水漱起口来。“&&&噗——”嘴巴里的许多牙菌连同水一起被吐了出来。&&&小熊知道嘴巴还有不少牙菌，&&&就举着牙刷又仔细地刷了一遍，&&&再漱口，这样重复了几遍，&&&直到嘴巴里的牙菌全被赶走为止。&&& “哈——”小熊轻轻哈了一口气。&&& “嗯，真香！”妈妈表扬道。&&& “嘻嘻！”小熊重新欢快起来，&&& “我以后每天都要坚持漱口、刷牙！&&&------end------”\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"file:///android_asset/story_icons/shuaya.png\",\n" +
                "                \"title\": \"天天刷牙好习惯\",\n" +
                "                \"voice\": \"\"\n" +
                "            }],\n" +
                "        \"type\": 1\n" +
                "    },\n" +
                "    \"error\": {\n" +
                "        \"code\": 200,\n" +
                "        \"msg\": \"\"\n" +
                "    }\n" +
                "}";
    }

    private String getDataType2(){

        return "{\n" +
                "    \"data\": {\n" +
                "        \"storys\": [\n" +
                "            {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"木匠和木头的故事：会说话的木头&&&木匠师傅得到了一根奇怪的木头：&&&像孩子一样，既会哭又会笑。&&&他是怎样得到的呢？&&&在古老的时候，有……&&& “是不是有一个国王？”小朋友们立即问道。&&&小朋友们，你们说错了。&&&在古老的时候，不是有一位国王，&&&也不是有一位王子或公主，而是有一根木头。&&&它是一根非常普通的木头，&&&是用来烧火的杂木。&&&在寒冷的冬天里，&&&人们为了取暖，&&&通常要在房屋里生炉子，&&&炉子里边烧的一般就是这种木头。&&&正是因为这根木头，&&&我们才有了下面这一个曲折、生动、有趣的故事。&&&在一个晴朗的日子里，&&&不知是什么原因，&&&一根木头“啪”地一声，&&&刚好落进一位老木匠的店铺里边。&&&那位老木匠的名字叫安东尼奥，&&&年龄大约有六十岁，&&&因为他的鼻尖老是紫红紫红的，&&&并且闪闪发亮，&&&好像是一颗熟透了的红樱桃。&&&所以，人们给他起了一个绰号&&&称他为“樱桃师傅”。&&&樱桃师傅正在店铺里面干活，&&&忽然听到“啪”的一声。&&&开始时吓了一大跳，&&&但当他发现是一根木头掉进来以后，又非常激动。&&&他兴奋得像孩子一样，&&&眉开眼笑，搓着双手&&&小声嘀咕着：&&& “这木头是从哪儿来的呢？&&&管他呢，我正准备做一条桌腿&&&这次可得到一根合适的木头。”&&&樱桃师傅马上提起一把非常锋利的斧子&&&准备把木头外面的那层皮削去，&&&先做出一个桌子腿的形状。&&&当樱桃师傅举起斧子正要砍下的时候，&&&突然耳边听到一个极为细小的声音，&&&央求着说：&&& “喂！请你手下留情，&&&砍轻一点儿！我怕疼啊！”&&&心地善良的樱桃师傅听到后大吃一惊，&&&举在半空中的斧子也停了下来。&&& “什么人？是谁在跟我说话呢？”&&&他睁大眼睛，在屋里四处搜寻，&&&可是连个人影儿也没有。&&&樱桃师傅心中非常纳闷：&&&这是谁的声音呢？&&&这个声音怎么这样陌生？&&&随后，他又趴到工作台下，&&&打开放衣服的柜门，&&&揭起装锯末和刨花儿的箱盖……&&&没有，根本就没人。&&&最后，他干脆开门出去，&&&向街上望去，&&&还是没发现一个人。&&&可是，这声音究竟是从哪儿传来的呢？&&& “我知道了！” &&&樱桃师傅搔搔假发，&&&笑着自言自语地说道：&&& “唉，上了年纪的人了，耳朵不好使了，&&&肯定是自个儿的幻觉，接着再来吧。”&&&樱桃师傅这样想，&&&心中松了一口气，&&&又举起斧头，大吼了一声，&&&再次用力向那根木头砍下去。&&& “哎哟，天哪！疼死我了，&&&你就不能轻一点吗？你砍伤我了！”&&&樱桃师傅一面哼着歌，&&&一面将斧子丢到一旁，&&&又从工具箱里面找出一把刨子。&&&这一次他准备将那根木头弄得光溜溜的。&&&樱桃师傅把那根木头固定在工作台上，&&& “呼哧呼哧”地来回推动着刨子。&&&没推几下，那细小的声音又传入耳中，&&&只听那声音“咯咯咯咯”地笑着说：&&& “不要刨了，&&&停下来，快停下来吧，&&&你把我弄得痒死啦！”&&&不幸的是樱桃师傅听到这声音后，&&&身体就像突然被强烈的电流击中一样，&&&一下子仰面跌倒在地。&&&当他慢慢醒过来的时候，&&&发现自己躺在了地板上。&&&老木匠的脸完全变了色，&&&因为非常恐惧，&&&就连原来总是红得发紫的鼻尖，这会儿也变成了青白色。&&&------end------\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"file:///android_asset/story_icons/mutouren.png\",\n" +
                "                \"title\": \"木匠和木头的故事\",\n" +
                "                \"voice\": \"woodtall.mp3\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"小狗的火箭&&&动物们都集中在动物园的广场上。&&&小狗汪汪乘坐的火箭，&&&正要向星星上的国家出发。&&&拿着大钟表的狮子举起了手：&&&“3，2，1，出发——” &&&“砰！” 火箭带着橙黄色的尾巴，&&&飞向蓝蓝的天空。&&&一会儿，从火箭里传出信号，&&&“火箭运行顺利，一切正常。 &&&啊，太好了。&&&大家欢呼着，目不转睛地盯着电视，&&&期待看见汪汪在星星国顺利着陆。&&&坐在火箭上的汪汪，&&&清楚地映在电视里。&&&“星星国里也有兔子吗？”兔子问。&&&“星星国里也有狮子吗？”狮子问。&&&突然，汪汪乘的火箭咕噜一下，&&&来了个U字形的向右转弯，开始下落。&&&兔子吓得跳起来，&&&来不及关电视连忙跑向火箭起飞的地方。&&&狮子和熊，还有很多小动物都跑来了。 &&&这时，火箭掉了下来。&&&大家一动不动，盯着火箭。 &&&忽然，“咔嗒”一声，&&&火箭门开了，汪汪从门里慢慢走了出来，&&&还特别装模作样地跟大家打招呼：&&&“初次见面，星星国的各位先生们，你们好！” &&&大家全傻了。汪汪以为火箭到了星星国啦！ &&&“汪汪，不是那么回事。&&&火箭掉下来，回到原地来啦！&&&我是你的好朋友兔子呀！”&&&“我是你的好朋友狮子呀！”&&&大家说了半天，汪汪却回道：&&&“你们这么快就愿意跟我成为好朋友，&&&谢谢你们！” &&&这下子可麻烦了！&&&汪汪把这儿当成星星国了！&&&看来只有放一遍电视的重放才能让汪汪相信了。\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"file:///android_asset/story_icons/dogrocket.png\",\n" +
                "                \"title\": \"小狗的火箭\",\n" +
                "                \"voice\": \"dogrocket.mp3\"\n" +
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

    private String getDataType1(){

        return "{\n" +
                "    \"data\": {\n" +
                "        \"storys\": [\n" +
                "            {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"最好玩儿的国王&&& “陛下，该起床了。” &&&八达国的国王睁开眼睛，&&&看到了负责照料他起居的钝钝保姆那张笑容可掬的脸。&&& “什么，天已经亮了吗？”国王打着哈欠问。&&& “对，天亮了。您再不起床，&&&早餐就要凉了。”钝钝保姆说。&&&国王只好起来了。&&&他一边穿衣服，一边嘀咕：&&& “不知道为什么，我觉得还没睡饱。” &&&不过，国王就是国王，&&&每天都有很多的工作等着他去完成，&&&就算没睡够，也绝对不能赖床。&&&国王穿好衣服，戴好王冠，推开卧室的门……&&&浓重的夜色立刻扑面而来。&&&天还黑着呢！&&&国王一时没反应过来，&&&钝钝保姆已经在背后偷笑了：&&& “愚人节快乐，我亲爱的陛下！” &&&国王连忙去看日历。&&&啊，真的，今天是四月一号愚人节！&&&而现在的时间是凌晨四点。&&&好哇，他被钝钝保姆给骗了！&&&愚人节里，每个人都有权利说谎骗人，&&&被骗时绝对不能生气，&&&所以就算钝钝保姆欺骗的是国王，&&&也不会受到惩罚。这是传统。&&&不过，传统归传统，&&&我们八达国的国王可管不了这些。他气坏啦！&&&怎么能不生气呢？&&&几乎每年的愚人节，&&&国王都要上好几次当，&&&大大丢尽了国王的脸面。&&&而他对别人说的谎话，&&&总是一下子就会被拆穿。&&&去年愚人节后，国王暗暗下定决心：&&&明年可绝对不能再当大笨蛋！&&&谁知道今年的愚人节刚到，&&&他就被骗了！&&&国王恨透了愚人节。&&&他本来可以回被窝里继续睡觉的，&&&反正天还没亮嘛，&&&但他却跑到办公室去了。&&&他用最快的速度写好了一条布告，&&&内容是这样的：&&&愚人节实在是一个糟糕透顶的日子！&&&这个节日只会培养出一大堆的骗子！&&&没有人喜欢被欺骗，&&&所以愚人节的存在只会让人心情不好！&&&哦，你说愚人节被骗不能生气？&&&简直太过分了！&&&这该死的节日必须被废除！&&&是的，从今天起，&&&我们八达国将不再有愚人节！&&&谁要是敢在四月一号这天骗人，&&&就会被关进大牢一个星期！&&&请不要挑战法律！&&&落款是国王威严的签名。&&&国王写完这条布告后，&&&天刚蒙蒙亮。&&&他要赶在全国人民都醒来前，&&&赶紧把布告贴出去。&&&从这个角度来说，&&&一早被钝钝保姆骗醒也不是没有好处的。&&&清晨，国王的士兵们骑着快马，&&&把布告贴遍了大街小巷。&&&人民失望了。愚人节是多么好玩的节日呀！&&&这个节日一年只有一次，&&&所以他们早就准备好了一肚子的谎话，&&&要对家人和朋友说个痛快。&&&可现在，愚人节取消了。&&&谁要敢在这天说谎，还会被关进大牢！&&&哦，这多么可怕！&&&这天的八达国，&&&出现最多的是这样的对话：&&& “嘿，我要告诉你一件事情！” &&& “嘘，你确定你说的不是谎话吗？&&&如果是的话可别说！小心坐牢！” &&&因为国王的士兵们遍布八达国的每个地方，&&&他们的耳朵灵着呢！&&&不断有士兵回去报告国王：&&&截止到10点10分，&&& Z地区没有发现任何人说谎话……&&&虽然每个人都是一副憋得很难受的样子。&&&国王非常满意，他一整天都挂着笑容。&&&哈，早该这样了！&&&不用怕被人骗，也不用想办法去骗别人！&&&夕阳西下，愚人节……&&&不，四月一号要过去了。&&&国王正准备下班，&&&突然有士兵闯进了他的办公室：&&& “报告陛下，我们发现了一个说谎的人！” &&&国王大怒：“什么？&&&快把他带来！这还了得！&&&我非惩罚他不可！&&&要把他关进大牢！” &&&两名士兵答应着，带犯人去了。&&&不一会儿他们就回来了。&&&国王一看，傻眼了。&&&那个犯人竟然是他的儿子——八达国的小王子！&&&小王子很少出皇宫，&&&一天中的大部分时间，&&&他都待在游戏室里玩。&&&所以他完全不知道父王颁布了什么法令，&&&他只是突然想起来今天是愚人节，&&&于是兴冲冲地骗人去了……&&& “父王，他们为什么要抓我？快放开我呀！” &&&小王子哭哭啼啼的。&&& “陛下，如果没有别的吩咐，&&&我们就把他带去牢房了。” &&&公事公办的士兵一点儿也不给国王留面子。&&& “等一下！” &&&国王大声说，他的手心都出汗了。&&& “四月一号说谎要坐牢，这是您决定的呀！” &&&士兵提醒国王。&&& “咳咳，这你们也相信？” &&&国王突然换了一副面孔，&&&严肃的脸上堆满了狡黠的笑容，&&& “今天可是愚人节！” &&& “什么？” &&& “我的意思是，那条布告根本就是假的！&&&愚人节就是应该尽情骗人！&&&你们都被我给骗啦！哈哈哈！” &&&两名士兵你看看我，我看看你，&&&不约而同地露出了苦笑。&&&他们松开了小王子的手，&&&离开了国王的办公室。&&&一传十，十传百，&&&很快全国人民都听说了，&&&他们纷纷议论着：&&& “真不愧是陛下，居然想得出这种谎话！” &&& “真可恶呀！” &&& “没办法啦，愚人节被骗可是不能生气的哟！” &&& “说得对，哈哈哈……”&&&人们说着，笑着，&&&然后抓紧时间，尽情地骗人去了，&&&趁愚人节还没有完全结束……&&&从那以后，八达国再也没有传出“不许过愚人节”的说法，&&&相反，国王陛下在一个愚人节骗了全国人民的事迹，&&&永远流传了下去。&&&是的，他的确是用了最高明的谎话，&&&欺骗了全国的人民呀！\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"file:///android_asset/story_icons/funking.jpg\",\n" +
                "                \"title\": \"最好玩的国王\",\n" +
                "                \"voice\": \"funking.mp3\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"author\": \"\",\n" +
                "                \"content\": \"大头儿子小头爸爸:夜晚的小路&&&晚上，小头爸爸骑车带着大头儿子从郊外回来，&&&不料半路上自行车轮胎坏了，&&&这么晚，上哪儿去找修车铺？&&&他们只好推着车往家里走。&&&“要走很远很远的路吗？” &&&大头儿子担心自己会走不动。&&& “不远，我们穿小路，只要十几分钟就够了！” &&& “小路？就是树林里的小路吗？&&&那儿会不会有鬼？” &&&婶婶跟大头儿子讲的鬼一般都是在树林里。&&& “哈，有鬼才好呢，&&&鬼是白天睡觉，晚上出来，&&&说不定它们的修车铺倒开着，&&&那我们就可以请鬼帮我们把自行车修好了呢！” &&&小头爸爸说得真轻松。\u2028&&&一会儿他们就走进了树林里的小路。&&&树林里是没有路灯的，&&&开始他们还能借着从树叶间漏下来的月光，&&&看清前面的路。&&&可走着走着，&&&头顶上的树叶越来越密，&&&月光怎么也钻不进来了，&&&大头儿子觉得自己好像是在晚上还戴着墨镜走一样。&&& “小头爸爸，我什么也看不见了！” &&&大头儿子有点害怕。&&& “现在我们是在赶路，&&&又不是在开玩具展销会，&&&用不着看见什么。” &&&小头爸爸好像比鬼还要勇敢。&&&忽然，小头爸爸一下子站住，慢慢抬起小头往一棵树上看。&&& “小头爸爸，怎么啦？” &&&大头儿子害怕地靠紧爸爸，&&&声音都已经发颤了。&&& “你，你听……”&&&没想到小头爸爸的声音颤得还要厉害。&&&大头儿子果然听到了从树上传下来的笃笃笃笃声。&&& “你也害怕了？”大头儿子担心地问。&&& “我才不怕呢！不过我们人最好要有礼貌，&&&不要去惊动鬼。” &&&说完，小头爸爸就弯下腰，&&&轻轻地、轻轻地继续往前走。&&&可是树上的笃笃声好像盯住了他们，&&&他们走到哪儿，&&&笃笃声就跟到哪儿。&&&扑通！小头爸爸忽然摔了一跤，&&&连同自行车一起倒在地上，&&&大头儿子也倒在地上，&&&大头儿子刚要张嘴大哭，&&&被小头爸爸一把捂住：&&& “千万别出声，它们鬼多，我们人少。” &&&大头儿子这才没敢哭出来。&&& “大头儿子，婶婶有没有教你怎么捉鬼？”\u2028 &&& “教了，她说碰到鬼的时候不要害怕，&&&只要站住了，对着四面吐四口唾沫，&&&鬼就会跑开的。” &&& “哎呀，你怎么不早说？” &&&小头爸爸赶紧停下，&&& “噗，噗，噗，噗”转着身体朝四面连吐四口唾沫，&&&笃笃笃笃的声音果然没有了。&&& “嘿，这一招还真灵，&&&我们就用这一招来捉鬼吧！” &&&小头爸爸丢下自行车，&&&往前走几步，转着圈吐四口唾沫；&&&再走几步，转着圈再吐四口唾沫。&&&大头儿子也跟在后面这样做。&&&他们果然不害怕了，&&&竟大着嗓子对着黑乎乎的树上喊：&&& “鬼，我们不怕你！有本事你出来！” &&&他们光喊还嫌不够威风，&&&又用脚去嗵嗵地踢树，&&&好像非要把鬼从树上踢下来不可。&&& “扑棱扑棱……”忽然从树上飞出一样东西。&&& “小头爸爸，你看鬼逃跑了！” &&& “原来是一只会飞的啄木鸟鬼！” &&&小头爸爸说完和大头儿子一起哈哈大笑起来。\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"file:///android_asset/story_icons/bigsmall.jpg\",\n" +
                "                \"title\": \"大头儿子和小头爸爸\",\n" +
                "                \"voice\": \"bigsmall.mp3\"\n" +
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

    private String getDataType200(){

        return "{\n" +
                "    \"data\": {\n" +
                "        \"storys\": [\n" +
                "            {\n" +
                "                \"author\": \"未知\",\n" +
                "                \"content\": \"最好玩儿的国王&&& “陛下，该起床了。” &&&八达国的国王睁开眼睛，&&&看到了负责照料他起居的钝钝保姆那张笑容可掬的脸。&&& “什么，天已经亮了吗？”国王打着哈欠问。&&& “对，天亮了。您再不起床，&&&早餐就要凉了。”钝钝保姆说。&&&国王只好起来了。&&&他一边穿衣服，一边嘀咕：&&& “不知道为什么，我觉得还没睡饱。” &&&不过，国王就是国王，&&&每天都有很多的工作等着他去完成，&&&就算没睡够，也绝对不能赖床。&&&国王穿好衣服，戴好王冠，推开卧室的门……&&&浓重的夜色立刻扑面而来。&&&天还黑着呢！&&&国王一时没反应过来，&&&钝钝保姆已经在背后偷笑了：&&& “愚人节快乐，我亲爱的陛下！” &&&国王连忙去看日历。&&&啊，真的，今天是四月一号愚人节！&&&而现在的时间是凌晨四点。&&&好哇，他被钝钝保姆给骗了！&&&愚人节里，每个人都有权利说谎骗人，&&&被骗时绝对不能生气，&&&所以就算钝钝保姆欺骗的是国王，&&&也不会受到惩罚。这是传统。&&&不过，传统归传统，&&&我们八达国的国王可管不了这些。他气坏啦！&&&怎么能不生气呢？&&&几乎每年的愚人节，&&&国王都要上好几次当，&&&大大丢尽了国王的脸面。&&&而他对别人说的谎话，&&&总是一下子就会被拆穿。&&&去年愚人节后，国王暗暗下定决心：&&&明年可绝对不能再当大笨蛋！&&&谁知道今年的愚人节刚到，&&&他就被骗了！&&&国王恨透了愚人节。&&&他本来可以回被窝里继续睡觉的，&&&反正天还没亮嘛，&&&但他却跑到办公室去了。&&&他用最快的速度写好了一条布告，&&&内容是这样的：&&&愚人节实在是一个糟糕透顶的日子！&&&这个节日只会培养出一大堆的骗子！&&&没有人喜欢被欺骗，&&&所以愚人节的存在只会让人心情不好！&&&哦，你说愚人节被骗不能生气？&&&简直太过分了！&&&这该死的节日必须被废除！&&&是的，从今天起，&&&我们八达国将不再有愚人节！&&&谁要是敢在四月一号这天骗人，&&&就会被关进大牢一个星期！&&&请不要挑战法律！&&&落款是国王威严的签名。&&&国王写完这条布告后，&&&天刚蒙蒙亮。&&&他要赶在全国人民都醒来前，&&&赶紧把布告贴出去。&&&从这个角度来说，&&&一早被钝钝保姆骗醒也不是没有好处的。&&&清晨，国王的士兵们骑着快马，&&&把布告贴遍了大街小巷。&&&人民失望了。愚人节是多么好玩的节日呀！&&&这个节日一年只有一次，&&&所以他们早就准备好了一肚子的谎话，&&&要对家人和朋友说个痛快。&&&可现在，愚人节取消了。&&&谁要敢在这天说谎，还会被关进大牢！&&&哦，这多么可怕！&&&这天的八达国，&&&出现最多的是这样的对话：&&& “嘿，我要告诉你一件事情！” &&& “嘘，你确定你说的不是谎话吗？&&&如果是的话可别说！小心坐牢！” &&&因为国王的士兵们遍布八达国的每个地方，&&&他们的耳朵灵着呢！&&&不断有士兵回去报告国王：&&&截止到10点10分，&&& Z地区没有发现任何人说谎话……&&&虽然每个人都是一副憋得很难受的样子。&&&国王非常满意，他一整天都挂着笑容。&&&哈，早该这样了！&&&不用怕被人骗，也不用想办法去骗别人！&&&夕阳西下，愚人节……&&&不，四月一号要过去了。&&&国王正准备下班，&&&突然有士兵闯进了他的办公室：&&& “报告陛下，我们发现了一个说谎的人！” &&&国王大怒：“什么？&&&快把他带来！这还了得！&&&我非惩罚他不可！&&&要把他关进大牢！” &&&两名士兵答应着，带犯人去了。&&&不一会儿他们就回来了。&&&国王一看，傻眼了。&&&那个犯人竟然是他的儿子——八达国的小王子！&&&小王子很少出皇宫，&&&一天中的大部分时间，&&&他都待在游戏室里玩。&&&所以他完全不知道父王颁布了什么法令，&&&他只是突然想起来今天是愚人节，&&&于是兴冲冲地骗人去了……&&& “父王，他们为什么要抓我？快放开我呀！” &&&小王子哭哭啼啼的。&&& “陛下，如果没有别的吩咐，&&&我们就把他带去牢房了。” &&&公事公办的士兵一点儿也不给国王留面子。&&& “等一下！” &&&国王大声说，他的手心都出汗了。&&& “四月一号说谎要坐牢，这是您决定的呀！” &&&士兵提醒国王。&&& “咳咳，这你们也相信？” &&&国王突然换了一副面孔，&&&严肃的脸上堆满了狡黠的笑容，&&& “今天可是愚人节！” &&& “什么？” &&& “我的意思是，那条布告根本就是假的！&&&愚人节就是应该尽情骗人！&&&你们都被我给骗啦！哈哈哈！” &&&两名士兵你看看我，我看看你，&&&不约而同地露出了苦笑。&&&他们松开了小王子的手，&&&离开了国王的办公室。&&&一传十，十传百，&&&很快全国人民都听说了，&&&他们纷纷议论着：&&& “真不愧是陛下，居然想得出这种谎话！” &&& “真可恶呀！” &&& “没办法啦，愚人节被骗可是不能生气的哟！” &&& “说得对，哈哈哈……”&&&人们说着，笑着，&&&然后抓紧时间，尽情地骗人去了，&&&趁愚人节还没有完全结束……&&&从那以后，八达国再也没有传出“不许过愚人节”的说法，&&&相反，国王陛下在一个愚人节骗了全国人民的事迹，&&&永远流传了下去。&&&是的，他的确是用了最高明的谎话，&&&欺骗了全国的人民呀！\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"file:///android_asset/story_icons/123.png\",\n" +
                "                \"title\": \"最好玩的国王\",\n" +
                "                \"voice\": \"funking.mp3\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"author\": \"未知\",\n" +
                "                \"content\": \"木匠和木头的故事：会说话的木头&&&木匠师傅得到了一根奇怪的木头：&&&像孩子一样，既会哭又会笑。&&&他是怎样得到的呢？&&&在古老的时候，有……&&& “是不是有一个国王？”小朋友们立即问道。&&&小朋友们，你们说错了。&&&在古老的时候，不是有一位国王，&&&也不是有一位王子或公主，而是有一根木头。&&&它是一根非常普通的木头，&&&是用来烧火的杂木。&&&在寒冷的冬天里，&&&人们为了取暖，&&&通常要在房屋里生炉子，&&&炉子里边烧的一般就是这种木头。&&&正是因为这根木头，&&&我们才有了下面这一个曲折、生动、有趣的故事。&&&在一个晴朗的日子里，&&&不知是什么原因，&&&一根木头“啪”地一声，&&&刚好落进一位老木匠的店铺里边。&&&那位老木匠的名字叫安东尼奥，&&&年龄大约有六十岁，&&&因为他的鼻尖老是紫红紫红的，&&&并且闪闪发亮，&&&好像是一颗熟透了的红樱桃。&&&所以，人们给他起了一个绰号&&&称他为“樱桃师傅”。&&&樱桃师傅正在店铺里面干活，&&&忽然听到“啪”的一声。&&&开始时吓了一大跳，&&&但当他发现是一根木头掉进来以后，又非常激动。&&&他兴奋得像孩子一样，&&&眉开眼笑，搓着双手&&&小声嘀咕着：&&& “这木头是从哪儿来的呢？&&&管他呢，我正准备做一条桌腿&&&这次可得到一根合适的木头。”&&&樱桃师傅马上提起一把非常锋利的斧子&&&准备把木头外面的那层皮削去，&&&先做出一个桌子腿的形状。&&&当樱桃师傅举起斧子正要砍下的时候，&&&突然耳边听到一个极为细小的声音，&&&央求着说：&&& “喂！请你手下留情，&&&砍轻一点儿！我怕疼啊！”&&&心地善良的樱桃师傅听到后大吃一惊，&&&举在半空中的斧子也停了下来。&&& “什么人？是谁在跟我说话呢？”&&&他睁大眼睛，在屋里四处搜寻，&&&可是连个人影儿也没有。&&&樱桃师傅心中非常纳闷：&&&这是谁的声音呢？&&&这个声音怎么这样陌生？&&&随后，他又趴到工作台下，&&&打开放衣服的柜门，&&&揭起装锯末和刨花儿的箱盖……&&&没有，根本就没人。&&&最后，他干脆开门出去，&&&向街上望去，&&&还是没发现一个人。&&&可是，这声音究竟是从哪儿传来的呢？&&& “我知道了！” &&&樱桃师傅搔搔假发，&&&笑着自言自语地说道：&&& “唉，上了年纪的人了，耳朵不好使了，&&&肯定是自个儿的幻觉，接着再来吧。”&&&樱桃师傅这样想，&&&心中松了一口气，&&&又举起斧头，大吼了一声，&&&再次用力向那根木头砍下去。&&& “哎哟，天哪！疼死我了，&&&你就不能轻一点吗？你砍伤我了！”&&&樱桃师傅一面哼着歌，&&&一面将斧子丢到一旁，&&&又从工具箱里面找出一把刨子。&&&这一次他准备将那根木头弄得光溜溜的。&&&樱桃师傅把那根木头固定在工作台上，&&& “呼哧呼哧”地来回推动着刨子。&&&没推几下，那细小的声音又传入耳中，&&&只听那声音“咯咯咯咯”地笑着说：&&& “不要刨了，&&&停下来，快停下来吧，&&&你把我弄得痒死啦！”&&&不幸的是樱桃师傅听到这声音后，&&&身体就像突然被强烈的电流击中一样，&&&一下子仰面跌倒在地。&&&当他慢慢醒过来的时候，&&&发现自己躺在了地板上。&&&老木匠的脸完全变了色，&&&因为非常恐惧，&&&就连原来总是红得发紫的鼻尖，这会儿也变成了青白色。&&&------end------\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"file:///android_asset/story_icons/456.png\",\n" +
                "                \"title\": \"木匠和木头的故事\",\n" +
                "                \"voice\": \"woodtall.mp3\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"author\": \"安徒生\",\n" +
                "                \"content\": \"大头儿子小头爸爸:夜晚的小路&&&晚上，小头爸爸骑车带着大头儿子从郊外回来，&&&不料半路上自行车轮胎坏了，&&&这么晚，上哪儿去找修车铺？&&&他们只好推着车往家里走。&&&“要走很远很远的路吗？” &&&大头儿子担心自己会走不动。&&& “不远，我们穿小路，只要十几分钟就够了！” &&& “小路？就是树林里的小路吗？&&&那儿会不会有鬼？” &&&婶婶跟大头儿子讲的鬼一般都是在树林里。&&& “哈，有鬼才好呢，&&&鬼是白天睡觉，晚上出来，&&&说不定它们的修车铺倒开着，&&&那我们就可以请鬼帮我们把自行车修好了呢！” &&&小头爸爸说得真轻松。\u2028&&&一会儿他们就走进了树林里的小路。&&&树林里是没有路灯的，&&&开始他们还能借着从树叶间漏下来的月光，&&&看清前面的路。&&&可走着走着，&&&头顶上的树叶越来越密，&&&月光怎么也钻不进来了，&&&大头儿子觉得自己好像是在晚上还戴着墨镜走一样。&&& “小头爸爸，我什么也看不见了！” &&&大头儿子有点害怕。&&& “现在我们是在赶路，&&&又不是在开玩具展销会，&&&用不着看见什么。” &&&小头爸爸好像比鬼还要勇敢。&&&忽然，小头爸爸一下子站住，慢慢抬起小头往一棵树上看。&&& “小头爸爸，怎么啦？” &&&大头儿子害怕地靠紧爸爸，&&&声音都已经发颤了。&&& “你，你听……”&&&没想到小头爸爸的声音颤得还要厉害。&&&大头儿子果然听到了从树上传下来的笃笃笃笃声。&&& “你也害怕了？”大头儿子担心地问。&&& “我才不怕呢！不过我们人最好要有礼貌，&&&不要去惊动鬼。” &&&说完，小头爸爸就弯下腰，&&&轻轻地、轻轻地继续往前走。&&&可是树上的笃笃声好像盯住了他们，&&&他们走到哪儿，&&&笃笃声就跟到哪儿。&&&扑通！小头爸爸忽然摔了一跤，&&&连同自行车一起倒在地上，&&&大头儿子也倒在地上，&&&大头儿子刚要张嘴大哭，&&&被小头爸爸一把捂住：&&& “千万别出声，它们鬼多，我们人少。” &&&大头儿子这才没敢哭出来。&&& “大头儿子，婶婶有没有教你怎么捉鬼？”\u2028 &&& “教了，她说碰到鬼的时候不要害怕，&&&只要站住了，对着四面吐四口唾沫，&&&鬼就会跑开的。” &&& “哎呀，你怎么不早说？” &&&小头爸爸赶紧停下，&&& “噗，噗，噗，噗”转着身体朝四面连吐四口唾沫，&&&笃笃笃笃的声音果然没有了。&&& “嘿，这一招还真灵，&&&我们就用这一招来捉鬼吧！” &&&小头爸爸丢下自行车，&&&往前走几步，转着圈吐四口唾沫；&&&再走几步，转着圈再吐四口唾沫。&&&大头儿子也跟在后面这样做。&&&他们果然不害怕了，&&&竟大着嗓子对着黑乎乎的树上喊：&&& “鬼，我们不怕你！有本事你出来！” &&&他们光喊还嫌不够威风，&&&又用脚去嗵嗵地踢树，&&&好像非要把鬼从树上踢下来不可。&&& “扑棱扑棱……”忽然从树上飞出一样东西。&&& “小头爸爸，你看鬼逃跑了！” &&& “原来是一只会飞的啄木鸟鬼！” &&&小头爸爸说完和大头儿子一起哈哈大笑起来。\",\n" +
                "                \"id\": \"\",\n" +
                "                \"img\": \"file:///android_asset/story_icons/789.png\",\n" +
                "                \"title\": \"大头儿子和小头爸爸\",\n" +
                "                \"voice\": \"bigsmall.mp3\"\n" +
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
                "                \"img\": \"file:///android_asset/banner_icons/banner01.jpg\",\n" +
                "                \"url\": \"http://news.dahe.cn/2017/02-16/108287701.html\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"desc\": \"banner02\",\n" +
                "                \"img\": \"file:///android_asset/banner_icons/banner02.jpg\",\n" +
                "                \"url\": \"http://chanye.07073.com/guonei/1553341.html\"\n" +
                "            },\n" +
                "            {\n" +
                "                \"desc\": \"banner03\",\n" +
                "                \"img\": \"file:///android_asset/banner_icons/banner03.jpg\",\n" +
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

    public String getCates() {
        return "{\n" +
                "    \"data\": {\n" +
                "        \"category\": [\n" +
                "            {\n" +
                "                \"cat\": \"热门\",\n" +
                "                \"list\": [\n" +
                "                    \"凯叔365夜\",\n" +
                "                    \"新黑猫警长\",\n" +
                "                    \"猫力猫力\",\n" +
                "                    \"恐龙大陆\",\n" +
                "                    \"卡梅拉\",\n" +
                "                    \"灯塔守护人\"\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"cat\": \"年龄\",\n" +
                "                \"list\": [\n" +
                "                    \"0-2岁\",\n" +
                "                    \"3岁+\",\n" +
                "                    \"6岁+\",\n" +
                "                    \"全龄精选\"\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"cat\": \"功能\",\n" +
                "                \"list\": [\n" +
                "                    \"哄睡神器\",\n" +
                "                    \"入园引导\",\n" +
                "                    \"学前准备\",\n" +
                "                    \"轻松出行\",\n" +
                "                    \"凯叔叫早\",\n" +
                "                    \"亲子时光\",\n" +
                "                    \"自我认知\",\n" +
                "                    \"习惯养成\",\n" +
                "                    \"性格培养\",\n" +
                "                    \"情绪管理\",\n" +
                "                    \"社交能力\",\n" +
                "                    \"性教育\",\n" +
                "                    \"尿尿屁\",\n" +
                "                    \"调皮捣蛋\",\n" +
                "                    \"生命教育\"\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"cat\": \"类型\",\n" +
                "                \"list\": [\n" +
                "                    \"奇幻冒险\",\n" +
                "                    \"科普故事\",\n" +
                "                    \"国学经典\",\n" +
                "                    \"文学名著\",\n" +
                "                    \"民间传说\",\n" +
                "                    \"绘本故事\"\n" +
                "                ]\n" +
                "            },\n" +
                "            {\n" +
                "                \"cat\": \"素材\",\n" +
                "                \"list\": [\n" +
                "                    \"哄睡音乐\",\n" +
                "                    \"睡前诗\"\n" +
                "                ]\n" +
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
