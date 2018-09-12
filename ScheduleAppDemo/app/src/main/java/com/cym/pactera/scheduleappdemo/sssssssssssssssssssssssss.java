//////方法一:使用poi搜索接口方法
//private PoiResult poiResult; // poi返回的结果
//private int currentPage = 0;// 当前页面，从0开始计数
//private PoiSearch.Query query;// Poi查询条件类
//private LatLonPoint latLonPoint;
//private PoiSearch poiSearch;
//private List<PoiItem> poiItems;// poi数据
//private String keyWord;
//private CommonAdapter adapter;
//private final int ADDRESS_LOCATION_GET = 3242;
//private String POI_SEARCH_TYPE = "汽车服务|汽车销售|" +
//        "//汽车维修|摩托车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|" +
//        "//住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|" +
//        "//金融保险服务|公司企业|道路附属设施|地名地址信息|公共设施";
//
//        ......
//
///**
// * 开始进行poi搜索
// */
//protected void doSearchQuery() {
//        latLonPoint = new LatLonPoint(MyApplication.mapLocation.getLatitude(), MyApplication.mapLocation.getLongitude());// 116.472995,39.993743
//
//        keyWord = inputText.getText().toString().trim();
//        currentPage = 0;
//        //keyWord表示搜索字符串，
//        //第二个参数表示POI搜索类型，二者选填其一，选用POI搜索类型时建议填写类型代码，码表可以参考下方（而非文字）
//        //cityCode表示POI搜索区域，可以是城市编码也可以是城市名称，也可以传空字符串，空字符串代表全国在全国范围内进行搜索
//        query = new PoiSearch.Query(keyWord, POI_SEARCH_TYPE, "");
//        query.setPageSize(30);// 设置每页最多返回多少条poiItem
//        query.setPageNum(currentPage);// 设置查第一页
//        if (latLonPoint != null) {
//        poiSearch = new PoiSearch(this, query);
//        poiSearch.setOnPoiSearchListener(this);
//        poiSearch.setBound(new PoiSearch.SearchBound(latLonPoint, 3000, true));//设置搜索范围
//        poiSearch.searchPOIAsyn();// 异步搜索
//        }
//
//        }
//
//        ......
//
//@Override
//public void onPoiSearched(PoiResult result, int code) {
//
//        //DialogUtils.dismissProgressDialog();
//        if (code == AMapException.CODE_AMAP_SUCCESS) {
//        if (result != null && result.getQuery() != null) {// 搜索poi的结果
//        loge("搜索的code为===="+code+", result数量=="+result.getPois().size());
//        if (result.getQuery().equals(query)) {// 是否是同一次搜索
//        poiResult = result;
//        loge("搜索的code为===="+code+", result数量=="+poiResult.getPois().size());
//        List<SuggestionCity> suggestionCities = poiResult.getSearchSuggestionCitys();// 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
//        if (poiItems != null && poiItems.size() > 0) {
//        poiItems.clear();
//        if (adapter != null) {
//        adapter.notifyDataSetChanged();
//        }
//        }
//        poiItems = poiResult.getPois();// 取得第一页的poiitem数据，页数从数字0开始
//
//        //通过listview显示搜索结果的操作省略
//        ......
//        }
//        } else {
//        loge("没有搜索结果");
//        toast(getString(R.string.search_no_result));
//        empty_view.setText(getString(R.string.search_no_result));
//        }
//        } else {
//        loge("搜索出现错误");
//        toast(getString(R.string.search_error));
//        empty_view.setText(getString(R.string.search_error));
//        }
//
//        }
//
//@Override
//public void onPoiItemSearched(PoiItem poiItem, int i) {
//
//        }
