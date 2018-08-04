package cn.edu.nju.flowerstory.app;

import android.os.Environment;

/**
 *
 * Created by Administrator on 2018/4/8 0008.
 */
public class Constants {

    public static final int MAX_PREVIEW_WIDTH = 1920;
    public static final int MAX_PREVIEW_HEIGHT = 1080;

    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;

    public static final int TAB_SIZE = 5;
    public static final String TAB_TITLE[] = {"首页", "美图", "百科", "养护",  "收藏"};
    public static final int CURRENT_ITEM_INDEX = 0;

    public static final int HANDLER_CALLBACK_SUCCESS = 1;
    public static final int HANDLER_CALLBACK_FAILURE = 2;
    public static final int MAIN_VIEW_PAGER_CURRENT_ITEM = 1;
    public static final int TAKE_PHOTO = 1;
    public static final int CUT_PHOTO = 2;
    public static final int SELECT_PHOTO = 3;
    public static final int CAMERA = 4;
    public static final int CUT_PHOTO_DONE = 5;

    public static final int PERMISSIONS_GRANTED = 0;
    public static final int PERMISSIONS_DENIED = 1;

    public static final String DIR_PATH = Environment.getExternalStorageDirectory().getPath()+"/cn.edu.nju.flowerstory";
    public static final int SUB_DIR_PATH_SIZE = 2;
    public static final String[] SUB_DIR_PATH = {DIR_PATH+"/image", DIR_PATH+"/log"};

    public static final int CAMERA_SETIMAGE = 1;
    public static final int CAMERA_MOVE_FOCK = 2;
    public static final int CAMERA_RESULT = 3 ;
    public static final int CAMERA_SELECTED = 4;

    public static final String[] FLOWERD = {
            "Rosa rugosa Thunb.",
            "Cymbidium ssp.",
            "Paeonia suffruticosa Andr.",
            "Helianthus annuusL.",
            "Cerasus sp.",
            "Brassica campestris"
    };

    public static final String[] FLOWER_NAME = {"玫瑰", "兰花", "牡丹", "向日葵", "樱花", "油菜花"};
    public static final String[] FLOWER = {
            "玫瑰（学名：Rosa rugosa Thunb.）：原产地中国。属蔷薇目，蔷薇科落叶灌木，枝杆多针刺，奇数羽状复叶，小叶5-9片，椭圆形，有边刺。花瓣倒卵形，重瓣至半重瓣，花有紫红色、白色，果期8-9月，扁球形。枝条较为柔弱软垂且多密刺，每年花期只有一次，因此较少用于育种，近来其主要被重视的特性为抗病性与耐寒性。\n\n" +
                    "在欧洲诸语言中，蔷薇、玫瑰、月季都是使用同一个词，如英语是rose，德语是Die Rose。玫瑰是英国的国花。通俗意义中的“玫瑰”已成为多种蔷薇属植物的通称。\n\n" +
                    "形态特征\n直立灌木，高可达2米；茎粗壮，丛生；小枝密被绒毛，并有针刺和腺毛，有直立或弯曲、淡黄色的皮刺，皮刺外被绒毛。小叶5-9，连叶柄长5-13厘米；小叶片椭圆形或椭圆状倒卵形，长1.5-4.5厘米，宽1-2.5厘米，先端急尖或圆钝，基部圆形或宽楔形，边缘有尖锐锯齿，上面深绿色，无毛，叶脉下陷，有褶皱，下面灰绿色，中脉突起，网脉明显，密被绒毛和腺毛，有时腺毛不明显；叶柄和叶轴密被绒毛和腺毛；托叶大部贴生于叶柄，离生部分卵形，边缘有带腺锯齿，下面被绒毛。\n\n" +
                    "寓意\n在中国，玫瑰则因其枝茎带刺，被认为是刺客、侠客的象征。而在西方则把玫瑰花当作严守秘密的象征，做客时看到主人家桌子上方画有玫瑰，就明白在这桌上所谈的一切均不可外传，于是有了Sub rosa，“在玫瑰花底下”这个拉丁成语。 英语的under the rose则是源自德语unter der Rosen，古代德国的宴会厅、会议室以及酒店餐厅，天花板上常画有或刻有玫瑰花，用来提醒与会者守口如瓶，严守秘密，不要把玫瑰花下的言行透露出去。" +
                    "这是起源于罗马神话中的荷鲁斯（Horus）撞见美女——爱的女神“维纳斯”偷情的情事，她儿子丘比特为了帮自己的母亲保有名节，于是给了他一朵玫瑰，请他守口如瓶，荷鲁斯收了玫瑰于是缄默不语，成为“沉默之神”，这就是under the rose之所以为守口如瓶的由来。",

            "兰花（学名：Cymbidium ssp.）：附生或地生草本，叶数枚至多枚，通常生于假鳞茎基部或下部节上，二列，带状或罕有倒披针形至狭椭圆形，基部一般有宽阔的鞘并围抱假鳞茎，有关节。总状花序具数花或多花，颜色有白、纯白、白绿、黄绿、淡黄、淡黄褐、黄、红、青、紫。\n\n" +
                    "中国传统名花中的兰花仅指分布在中国兰属植物中的若干种地生兰，如春兰、惠兰、建兰、墨兰和寒兰等，即通常所指的“中国兰”。这一类兰花与花大色艳的热带兰花大不相同，没有醒目的艳态，没有硕大的花、叶，却具有质朴文静、淡雅高洁的气质，很符合东方人的审美标准。在中国有一千余年的栽培历史。\n\n" +
                    "附生或地生草本，罕有腐生，通常具假鳞茎；假鳞茎卵球形、椭圆形或梭形，较少不存在或延长成茎状，通常包藏于叶基部的鞘之内。叶数枚至多枚，通常生于假鳞茎基部或下部节上，二列，带状或罕有倒披针形至狭椭圆形，基部一般有宽阔的鞘并围抱假鳞茎，有关节。\n\n" +
                    "自古以来中国人民爱兰、养兰、咏兰、画兰，古人曾有“观叶胜观花”的赞叹。人们更欣赏兰花以草木为伍，不与群芳争艳，不畏霜雪欺凌，坚忍不拔的刚毅气质，“芝兰生于深谷，不以无人而不芳”。兰花历来被人们当作高洁、典雅的象征，与梅花、竹、菊花一起被人们称为“四君子”。",

            "牡丹（学名：Paeonia suffruticosa Andr.）是芍药科、芍药属植物，为多年生落叶灌木。茎高达2米；分枝短而粗。叶通常为二回三出复叶，偶尔近枝顶的叶为3小叶；顶生小叶宽卵形，表面绿色，无毛，背面淡绿色，有时具白粉，侧生小叶狭卵形或长圆状卵形，叶柄长5-11厘米，和叶轴均无毛。花单生枝顶，苞片5，长椭圆形；萼片5，绿色，宽卵形，花瓣5或为重瓣，玫瑰色、红紫色、粉红色至白色，通常变异很大，倒卵形，顶端呈不规则的波状；花药长圆形，长4毫米；花盘革质，杯状，紫红色；心皮5，稀更多，密生柔毛。蓇葖长圆形，密生黄褐色硬毛。花期5月；果期6月。\n\n" +
                    "据《神农本草经》记载：“牡丹味辛寒，一名鹿韭，一名鼠姑，生山谷”。在甘肃省武威县发掘的东汉早期墓葬中，发现医学简数十枚，其中有牡丹治疗血瘀病的记载。牡丹原产于中国的长江流域与黄河流域诸省山间或丘岭中，人们发现了它的药用价值和观赏价值，而变野生为家养。从南北朝“永嘉水际竹间多牡丹“至今，栽培历史也有1500年了。在长期的栽培过程中，牡丹发生了变异，出现了许多花大色艳的品种，愈来愈受到人们的重视，其栽培范围由长江、黄河流域诸省向全国扩大。如今已扩展到中国东北、东南，以及内蒙、新疆、西藏、台湾等地。\n\n" +
                    "牡丹文化的起源，若从《诗经》牡丹进入诗歌，算起距今约3000年历史。秦汉时代以药用植物将牡丹记入《神农本草经》，牡丹已进入药物学。南北朝时，北齐杨子华画牡丹，牡丹已进入艺术领域。史书记载，隋炀帝在洛阳建西苑，诏天下进奇石花卉，易州进牡丹二十箱，植于西苑，自此，牡丹进入皇家园林，涉足园艺学。唐代，牡丹诗大量涌现，刘禹锡的“唯有牡丹真国色，花开时节动京城”，脍炙人口；李白的“云想衣裳花想容，春风拂槛露华浓”，千古绝唱。宋代开始，除牡丹诗词大量问世外，又出现了牡丹专著，诸如欧阳修的《洛阳牡丹记》、陆游的《天彭牡丹谱》、丘浚的《牡丹荣辱志》、张邦基的《陈州牡丹记》等，宋代有十几部。" +
                    "元姚遂有《序牡丹》，明人高濂有《牡丹花谱》、王象晋有《群芳谱》，薛凤翔有《亳州牡丹史》，清人汪灏有《广群芳谱》、苏毓眉有《曹南牡丹谱》、余鹏的有《曹州牡丹谱》、由于牡丹花花型优美，颜色绚丽、清雅，因此是当代画家们经常表现的题材，如余致贞、吴玉阳等。散见于历代种种杂著、文集中的牡丹诗词文赋，遍布民间花乡的牡丹传说故事，以及雕塑、雕刻、绘画、音乐、戏剧、服饰、起居、食品等方面的牡丹文化现象，数见不鲜。",

            "向日葵（拉丁文：Helianthus annuusL.），是菊科向日葵属的一年生草本植物。高1～3.5米。茎直立，圆形多棱角，质硬被白色粗硬毛。广卵形的叶片通常互生，先端锐突或渐尖，有基出3脉，边缘具粗锯齿，两面粗糙，被毛，有长柄。头状花序，直径10～30厘米，单生于茎顶或枝端。\n\n" +
                    "1年生草本，高1.0～3.5米，杂交品种有半米高植株。茎直立，粗壮，圆形多棱角，为白色粗硬毛。叶通常互生，心状 卵形或卵圆形，先端锐突或渐尖，有基出3脉，边缘具粗锯齿，两面粗糙，被毛，有长柄。\n\n" +
                    "向日葵约在明朝时引入中国，如今所知最早记载向日葵的文献为明朝人王象晋所著《群芳谱》（1621年），该书中尚无“向日葵”一名，只在“花谱三*菊”中附“丈菊”，原文如下：“丈菊－名本番菊－名迎阳花，茎长丈余，秆坚粗如竹，叶类麻，多直生，虽有分枝，只生一花大如盘盂，单瓣色黄心皆作窠如蜂房状，至秋渐紫黑而坚，取其子中之甚易生，花有毒能堕胎”。\n" +
                    "“向日”之名，见于文震亨《长物志》（约1635年左右）。\n" +
                    "1820年谢方在《花木小志》中言向日葵处处有之，既可观赏，又可食用。",

            "樱花（学名：Cerasus sp.）：是蔷薇科樱属几种植物的统称，在《中国植物志》新修订的名称中专指“东京樱花”，亦称“日本樱花”。樱花品种相当繁多，数目超过三百种以上，全世界共有野生樱花约150种，中国有50多种。全世界约40种樱花类植物野生种祖先中，原产于中国的有33种。其他的则是通过园艺杂交所衍生得到的品种。\n\n" +
                    "樱花是乔木，高4-16米，树皮灰色。小枝淡紫褐色，无毛，嫩枝绿色，被疏柔毛。冬芽卵圆形，无毛。叶片椭圆卵形或倒卵形，长5-12厘米，宽2.5-7厘米，先端渐尖或骤尾尖，基部圆形，稀楔形，边有尖锐重锯齿，齿端渐尖，有小腺体，上面深绿色，无毛，下面淡绿色，沿脉被稀疏柔毛，有侧脉7-10对；叶柄长1.3-1.5厘米，密被柔毛，顶端有1-2个腺体或有时无腺体；托叶披针形，有羽裂腺齿，被柔毛，早落。\n\n" +
                    "樱花是爱情与希望的象征，日本的代表物之一。在日本，相传在很久以前，日本有位名叫“木花开耶姬”（意为樱花）的仙女。有一年11月，仙女从冲绳出发，途经九州、关西、关东等地，在第二年5月到达北海道。沿途，她将一种象征爱情与希望的花朵撒遍每一个角落。为了纪念这位仙女，当地人将这种花命名为“樱花”，日本也因此成为“樱花之国”。",

            "油菜花，别名芸薹(YUN TAI)，拉丁文名Brassica campestris，原产地在欧洲与中亚一带，" +
                    "是一种十字花科的一年生草本植物。在我国集中在江西婺源篁（huáng）岭、汉中盆地和江岭万亩梯田、云南罗平平原、青海门源高原等。\n\n" +
                    "植株笔直丛生，茎绿花黄，基生叶呈旋叠状生长，茎生叶，一般是互生，没有托叶。花两性，辐射对称，花瓣4枚，呈十字形排列，花片质如宣纸，嫩黄微薄。雄蕊通常6枚，4长2短，通常称为“四强雄蕊”，雌蕊由二心皮构成，子房位置靠上。" +
                    "茎圆柱形，直径大多在0.1-0.8厘米之间，多分枝，茎杆较软；叶互生，叶大，浓绿色，无叶柄及托。三、四月间，茎梢着花，花为总状花序，花萼片四片，黄绿色，花冠四瓣，黄色，呈十字形。果实为长角果，到夏季，成熟时开裂散出种子，紫黑色，也有黄色。\n\n" +
                    "油菜花四片花瓣，整齐地围绕着花蕊，朴实个性。花瓣十分精致，有细细的纹路，那是技艺多么高超的雕刻家也无法雕琢出来的。中间的花蕊弯曲着凑在一块，仿佛在说着悄悄话。它有坚韧的根茎，茂密的叶，有着像栽种它们的农民们一样的淳朴与粗犷。\n\n" +
                    "油菜花具有重要的经济价值，又有观赏价值，是观光资源。油菜进入开花季节，田间一片金黄，余邵诗云：“油菜花开满地黄，丛间蝶舞蜜蜂忙；清风吹拂金波涌，飘溢醉人浓郁香”。油菜花竞相怒放，花粉中含有丰富的花蜜，引来彩蝶与蜜蜂飞舞花丛间。"+
                    "浓郁花香令人陶醉，美丽风景让人流连。"
    };

}
