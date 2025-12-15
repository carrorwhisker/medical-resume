// #ifdef APP-PLUS | MP
export const BAES_URL = "http://123.56.162.200:8080/Main/action/";
// #endif

// #ifdef H5
export const BAES_URL = "/api/";
// #endif

export const uploadFileUrl = "http://123.56.162.200:8080/common/upload"
export const uploadImgUrl = "http://123.56.162.200:8080/common/upload"


//请求路径
export const service = {
	login:"User/User/login",
	userInfo: "User/User/userInfo",
	updateUserInfo: "User/User/updateUserInfo",
	getCollectList: "User/User/getCollectList",
	rechargePay:"User/Usercharge/rechargePay",
	meetingPay:"User/Usercharge/meetingPay",
	
	
	getUserCardList:"Card/CardList/getUserCardList",

	getVipList: "Home/Home/getVipList",

	getBanner: "Home/Home/getBanner",
	homeCardList: "Card/CardList/homeCardList",
	
	

	saveCard: "Card/Card/saveCard",
	cardInfoList: "Card/Card/cardInfoList",
	departmentList: "Card/Card/departmentList",
	saveAuthentication:"Card/Card/saveAuthentication",
	getAuthentication:"Card/Card/getAuthentication",
	delAttachment:"Card/CardList/delAttachment",


	cardCollect: "Card/CardList/cardCollect",
	indexList: "Card/CardList/indexList",
	cardDetail: "Card/CardList/cardDetail",
	exchangeCard: "Card/CardList/exchangeCard",
	filterList: "Card/Card/filterList",
	departmentCardSave:"Card/CardList/departmentCardSave",
	
	
	getUserListMeeting:"Meeting/Meeting/getUserListMeeting",
	getMeetingList:"Meeting/Meeting/getMeetingList",
	getMeetingDetail:"Meeting/Meeting/getMeetingDetail",
	getMeetingSignUp:"Meeting/Meeting/getMeetingSignUp",
	getMeetingSignUpInfo:"Meeting/Meeting/getMeetingSignUpInfo",
	getMeetingSignUpTrip:"Meeting/Meeting/getMeetingSignUpTrip",
	
	cancelMeetingData:"Meeting/Meeting/cancelMeetingData",
	
}