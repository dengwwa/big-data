//package com.learn.spark.core
//
//
//import com.cmm.data.common.constant.es.ElasticSearchIndex
//import com.cmm.data.common.entity.dy.author.Author
//import com.cmm.data.common.hbase.api.HBaseManager
//import com.cqq.data.batch.author.DyAuthorSyncJob.Contact
//import com.cqq.data.domain.author.AuthorLabelV2
//import com.cqq.data.instance.MAIN_ES
//import com.cqq.data.util.{DateUtils, JsonUtils}
//import org.apache.poi.xssf.streaming.SXSSFWorkbook
//import org.elasticsearch.action.search.{ClearScrollRequest, SearchRequest, SearchScrollRequest}
//import org.elasticsearch.client.RequestOptions
//import org.elasticsearch.common.unit.TimeValue
//import org.elasticsearch.index.query.QueryBuilders
//import org.elasticsearch.search.Scroll
//import org.elasticsearch.search.builder.SearchSourceBuilder
//
//import java.io._
//import scala.collection.JavaConversions._
//
///**
// * 上海凌脉网络科技-达人资源包脚本
// *
// * @author xujinze
// * @date 2024/7/29
// */
//object LingMaiCustomizationScript {
//
//  def main(args: Array[String]): Unit = {
//    val path = args match {
//      case Array(path) => path
//      case Array() => throw new IllegalArgumentException
//    }
//    val started = System.currentTimeMillis
//    val authors = getAuthors
//    save(authors, path)
//    val elapsed = (System.currentTimeMillis - started) / 1000
//    println(s"elapsed: ${elapsed}s")
//  }
//
//  private def getAuthors: Iterable[AuthorProfile] = getAuthorProfiles(getAuthorIds)
//
//  private def getAuthorIds: Iterable[String] = {
//    val search = new SearchSourceBuilder
//    val query = QueryBuilders.boolQuery
//    query.must(QueryBuilders.termQuery("single_tags_v2.first", "游戏"))
//    query.must(QueryBuilders.rangeQuery("follower_count")
//      .gte(5000)
//      .lte(100000))
//    query.should(QueryBuilders.termQuery("contact", 1))
//    query.should(QueryBuilders.existsQuery("signature"))
//    query.minimumShouldMatch(1)
//
//    // 基础筛选
//    val expire = DateUtils.daysSub(DateUtils.curDate(), 90, DateUtils.dayFormatter)
//    query.must(QueryBuilders.rangeQuery("batch_time").gte(expire).boost(1.5f))
//    query.must(QueryBuilders.termQuery("exist", true))
//    query.must(QueryBuilders.termQuery("self_brand_code", ""))
//    query.must(QueryBuilders.termQuery("self_shop_id", ""))
//    query.mustNot(QueryBuilders.termQuery("verification_type", 2))
//
//    search.size(10000)
//      .query(query)
//      .fetchSource(Array("author_id"),
//        Array[String]())
//
//    val scroll = new Scroll(TimeValue.timeValueMinutes(10L))
//
//    val request = new SearchRequest(ElasticSearchIndex.TALENT.getEsIndex).source(search)
//      .scroll(scroll)
//
//    val authorIds = Iterable.newBuilder[String]
//    try {
//      val response = MAIN_ES.getClient.search(request, RequestOptions.DEFAULT)
//      var scrollId = response.getScrollId
//      var searchHits = response.getHits
//      if (searchHits.totalHits == 0) {
//        return Iterable()
//      }
//
//      searchHits.getHits
//        .map(hit => JsonUtils.readTree(hit.getSourceAsString)
//          .get("author_id")
//          .asText(""))
//        .filter(_ != "")
//        .foreach(authorIds += _)
//
//      while (searchHits != null && searchHits.nonEmpty) {
//        val request = new SearchScrollRequest(scrollId)
//        request.scroll(scroll)
//        val response = MAIN_ES.getClient.scroll(request, RequestOptions.DEFAULT)
//        scrollId = response.getScrollId
//        searchHits = response.getHits
//        if (searchHits.totalHits != 0) {
//          searchHits.getHits
//            .map(hit => JsonUtils.readTree(hit.getSourceAsString)
//              .get("author_id")
//              .asText(""))
//            .filter(_ != "")
//            .foreach(authorIds += _)
//        }
//      }
//
//      val clearRequest = new ClearScrollRequest
//      clearRequest.addScrollId(scrollId)
//      val clearResponse = MAIN_ES.getClient.clearScroll(clearRequest, RequestOptions.DEFAULT)
//      if (clearResponse.isSucceeded) {
//        println("clear successfully")
//      }
//
//      authorIds.result
//    } catch {
//      case e: Exception => {
//        e.printStackTrace()
//        Iterable()
//      }
//    } finally {
//      MAIN_ES.getClient.close()
//    }
//  }
//
//  private def getAuthorProfiles(unscoredRecommendAuthors: Iterable[String]): Iterable[AuthorProfile] = {
//    val client = HBaseManager.openClient
//    try {
//      unscoredRecommendAuthors.grouped(10000)
//        .map(ids => ids.map(new Author(_)))
//        .map(gets => client.gets(gets.toSeq, Array("authorId",
//          "nickname",
//          "signature",
//          "commonBringProductLabelV11",
//          "v3LabelSingle",
//          "reputationScore",
//          "followerCount",
//          "gender",
//          "age",
//          "province",
//          "city",
//          "fansGenderTypeV2",
//          "fansAgeTypeV2",
//          "fansProvinceTypeV2",
//          "fansCityTypeV2",
//          "mainBringProductType",
//          "authorBringLevel30",
//          "awemeCount30",
//          "bringAwemeCount30",
//          "awemeProductAmount30",
//          "awemeProductVolume30",
//          "liveCount30",
//          "liveAverageAmount30",
//          "liveAverageVolume30",
//          "liveAverageUser30",
//          "liveAverageOnline30",
//          "hasContact",
//          "phone",
//          "wechat",
//          "qq",
//          "email"
//        ).mkString(",")))
//        .flatMap(rs => {
//          rs.filter(r => r != null && r.getAuthorId.nonEmpty)
//            .map(r => {
//              val (hasContact, contactInfo) =
//                if (r.getHasContact != null && r.getHasContact.toInt == 1) {
//                  val contactInfo = JsonUtils.write(
//                    Contact(if (r.getPhone.nonEmpty) r.getPhone else null,
//                      if (r.getWechat.nonEmpty) r.getWechat else null,
//                      if (r.getQq.nonEmpty) r.getQq else null,
//                      if (r.getEmail.nonEmpty) r.getEmail else null))
//                  (1, contactInfo)
//                } else {
//                  (0, "{}")
//                }
//              AuthorProfile(r.getAuthorId,
//                r.getNickname,
//                r.getSignature,
//                if (r.getCommonBringProductLabelV11.nonEmpty) {
//                  JsonUtils.readSeq[String](r.getCommonBringProductLabelV11)
//                    .map(o => {
//                      val id = o.split("-")(0)
//                      id match {
//                        case "1" => (1, "母婴用品")
//                        case "2" => (2, "本地生活")
//                        case "3" => (3, "生鲜蔬果")
//                        case "4" => (4, "钟表配饰")
//                        case "5" => (5, "汽配摩托")
//                        case "6" => (6, "二手商品")
//                        case "7" => (7, "家具建材")
//                        case "8" => (8, "美妆护肤")
//                        case "9" => (9, "3C数码")
//                        case "10" => (10, "家居家纺")
//                        case "11" => (11, "礼品文创")
//                        case "12" => (12, "奢侈品")
//                        case "13" => (13, "运动户外")
//                        case "14" => (14, "图书教育")
//                        case "15" => (15, "玩具乐器")
//                        case "17" => (17, "食品饮料")
//                        case "18" => (18, "鞋靴箱包")
//                        case "19" => (19, "日用百货")
//                        case "20" => (20, "厨卫家电")
//                        case "21" => (21, "宠物用品")
//                        case "22" => (22, "服饰内衣")
//                        case "23" => (23, "鲜花绿植")
//                        case "24" => (24, "珠宝饰品")
//                        case "25" => (25, "医药保健")
//                        case _ => null
//                      }
//                    })
//                    .filter(_ != null)
//                    .toSeq
//                } else {
//                  Seq()
//                }, // categories_top
//                if (r.getV3LabelSingle.nonEmpty) {
//                  try {
//                    JsonUtils.read[AuthorLabelV2](r.getV3LabelSingle)
//                      .singleTagsV2
//                      .first
//                  } catch {
//                    case _: Exception => ""
//                  }
//                } else {
//                  ""
//                }, // label
//                r.getReputationScore,
//                r.getFollowerCount,
//                r.getGender,
//                r.getAge,
//                r.getProvince,
//                r.getCity,
//                r.getFansGenderTypeV2 match {
//                  case "男" => 0
//                  case "女" => 1
//                  case _ => 2
//                },
//                JsonUtils.readSeq[String](r.getFansAgeTypeV2),
//                JsonUtils.readSeq[String](r.getFansProvinceTypeV2),
//                JsonUtils.readSeq[String](r.getFansCityTypeV2),
//                r.getMainBringProductType,
//                if (r.getAwemeCount30 != null) {
//                  r.getAwemeCount30.toInt
//                } else 0,
//                if (r.getBringAwemeCount30 != 0) {
//                  r.getAwemeProductAmount30 / r.getBringAwemeCount30
//                } else 0D,
//                if (r.getBringAwemeCount30 != 0) {
//                  r.getAwemeProductVolume30 / r.getBringAwemeCount30
//                } else 0L,
//                if (r.getLiveCount30 != null) {
//                  r.getLiveCount30.toInt
//                } else 0,
//                r.getLiveAverageAmount30,
//                r.getLiveAverageVolume30,
//                if (r.getLiveAverageUser30 != null) {
//                  r.getLiveAverageUser30.toLong
//                } else 0L,
//                r.getLiveAverageOnline30,
//                hasContact,
//                contactInfo)
//            })
//        })
//        .toIterable
//    } finally {
//      client.close()
//    }
//  }
//
//  private def save(authors: Iterable[AuthorProfile], path: String): Unit = {
//    var workBook: SXSSFWorkbook = null
//    var os: FileOutputStream = null
//
//    try {
//      workBook = new SXSSFWorkbook
//      val sheet = workBook.createSheet
//
//      val headers = Array("达人id",
//        "昵称",
//        "简介",
//        "蝉妈妈达人页",
//        "主营类目",
//        "达人类型",
//        "带货口碑",
//        "粉丝数",
//        "达人性别",
//        "达人年龄",
//        "达人省份",
//        "达人城市",
//        "达人粉丝性别",
//        "达人粉丝年龄段",
//        "达人粉丝省份",
//        "达人粉丝城市",
//        "带货方式",
//        "近30天视频数",
//        "近30天视频平均成交额",
//        "近30天视频平均销量",
//        "近30天直播数",
//        "近30天直播平均成交额",
//        "近30天直播平均销量",
//        "近30天直播平均场观",
//        "近30天直播平均停留时长",
//        "是否有联系方式",
//        "联系方式")
//      val headerRow = sheet.createRow(0)
//      for (cn <- headers.indices) {
//        headerRow.createCell(cn).setCellValue(headers(cn))
//      }
//
//      var rn = 1
//      authors.toSeq
//        .foreach(r => {
//          val row = sheet.createRow(rn)
//          row.createCell(0).setCellValue(r.author_id)
//          row.createCell(1).setCellValue(r.nickname)
//          row.createCell(2).setCellValue(r.signature)
//          row.createCell(3).setCellValue(s"https://www.chanmama.com/authorDetail/${r.author_id}")
//          row.createCell(4).setCellValue(r.categories_top.map(_._2).mkString(","))
//          row.createCell(5).setCellValue(r.label)
//          row.createCell(6).setCellValue(r.reputation_score)
//          row.createCell(7).setCellValue(r.fans)
//          row.createCell(8).setCellValue(r.gender match {
//            case 0 => "男"
//            case 1 => "女"
//            case 2 => ""
//          })
//          row.createCell(9).setCellValue(r.age)
//          row.createCell(10).setCellValue(r.province)
//          row.createCell(11).setCellValue(r.city)
//          row.createCell(12).setCellValue(r.fans_gender match {
//            case 0 => "男"
//            case 1 => "女"
//            case 2 => ""
//          })
//          row.createCell(13).setCellValue(r.fans_ages.mkString(","))
//          row.createCell(14).setCellValue(r.fans_provinces.mkString(","))
//          row.createCell(15).setCellValue(r.fans_cities.mkString(","))
//          row.createCell(16).setCellValue(r.main_bring_product_type match {
//            case 0 => ""
//            case 1 => "视频"
//            case 2 => "直播"
//            case 3 => "视频/直播"
//            case 4 => "图文"
//          })
//          row.createCell(17).setCellValue(r.aweme_count_30)
//          row.createCell(18).setCellValue(r.aweme_avg_amount_30)
//          row.createCell(19).setCellValue(r.aweme_avg_volume_30)
//          row.createCell(20).setCellValue(r.live_count_30)
//          row.createCell(21).setCellValue(r.live_avg_amount_30)
//          row.createCell(22).setCellValue(r.live_avg_volume_30)
//          row.createCell(23).setCellValue(r.live_avg_user_30)
//          row.createCell(24).setCellValue(r.live_avg_online_30)
//          row.createCell(25).setCellValue(r.has_contact match {
//            case 0 => "无"
//            case 1 => "有"
//          })
//          row.createCell(26).setCellValue(r.contact_info)
//          rn += 1
//        })
//
//      os = new FileOutputStream(path)
//      workBook.write(os)
//    } finally {
//      os.close()
//      workBook.close()
//    }
//  }
//
//  private case class AuthorProfile(author_id: String,
//                                   nickname: String,
//                                   signature: String,
//                                   categories_top: Seq[(Int, String)],
//                                   label: String,
//                                   reputation_score: Double,
//                                   fans: Long,
//                                   gender: Int,
//                                   age: Int,
//                                   province: String,
//                                   city: String,
//                                   fans_gender: Int,
//                                   fans_ages: Seq[String],
//                                   fans_provinces: Seq[String],
//                                   fans_cities: Seq[String],
//                                   main_bring_product_type: Int,
//                                   aweme_count_30: Int,
//                                   aweme_avg_amount_30: Double,
//                                   aweme_avg_volume_30: Long,
//                                   live_count_30: Int,
//                                   live_avg_amount_30: Double,
//                                   live_avg_volume_30: Long,
//                                   live_avg_user_30: Long,
//                                   live_avg_online_30: Double,
//                                   has_contact: Int,
//                                   contact_info: String)
//}
