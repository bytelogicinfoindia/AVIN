package com.avin.avin.server;

public class ApiServer {


   // public static String BaseUrl="http://baccord.in/alien/index.php/alienApi/";
    public static String BaseUrl="http://buddhainstituteofhealtheducation.com/alien/index.php/alienApi/";

    public static class ServerKey{

        public static String  Login_Api=BaseUrl+"User_login/LoginApi";
        public static String  Register_Api=BaseUrl+"User_login/RegisterApi";
        public static String  UpdateProfileApi_Api=BaseUrl+"User_login/UpdateProfileApi";
        public static String  ChangePasswordApi_Api=BaseUrl+"User_login/ChangePasswordApi";
        public static String  ChangeProfileApi_Api=BaseUrl+"User_login/ChangeProfileApi";
        public static String  Booking_Api=BaseUrl+"User_booking/UserBookingrApi";
        public static String  AllBookingrApi_Api=BaseUrl+"User_booking/AllBookingrApi";
        public static String  BookingViewApi_Api_Api=BaseUrl+"User_booking/BookingViewApi";
        public static String  BookingCancelDeleteApi_Api=BaseUrl+"User_booking/BookingCancelDeleteApi";
        public static String  ReschedulsApi_Api=BaseUrl+"User_booking/ReschedulsApi";
        public static String  ListingApi_Api=BaseUrl+"Listing/Listing_Api";
        public static String  Category_SubApi_Api=BaseUrl+"Listing/Category_SubApi";
        public static String  SearchItemsApi_Api=BaseUrl+"Listing/SearchItemsApi";
        public static String  Banner_Api=BaseUrl+"Listing/Banner_Api";











        public static String imagefetch="http://buddhainstituteofhealtheducation.com/alien/uploads/";




// Otp send Api Login Time

        public static String  OTPApi1="http://sms.bulksmssale.com/app/smsapi/index.php?campaign=6478&routeid=100233&type=text&contacts=";

    }

    public static class ApiParams {


        public static String Phone = "phone";
        public static String Name = "name";
        public static String Password = "password";
        public static String Email = "email";
        public static String Address = "address";
        public static String Zip_code = "zip_code";
        public static String User_profile = "user_profile";
        public static String User_id = "user_id";
        public static String Date = "date";
        public static String Time = "time";
        public static String Service_id = "service_id";
        public static String Service_name = "service_name";
        public static String ID = "id";
        public static String sub_cat = "sub_cat";
        public static String Status = "status";
        public static String OrderNo = "orderno";



    }


}
