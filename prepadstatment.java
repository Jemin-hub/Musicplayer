import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


class pre {
    public static void main(String args[]) throws Exception {
        String url="jdbc:mysql://localhost:3306/kukadiya";
        String user="root";
        String pass="";
        String driver="con.mysql.cj.jdbc.Driver";
        Connection con=DriverManager.getConnection(url,user,pass);
        if(con!=null){
            System.out.println("connection is successful");
        }else{
            System.out.println("not connected");
        }
      /*   String sql="Insert into student values(?,?,?)";
        PreparedStatement pst=con.prepareStatement(sql);
         pst.setInt(1,5);
        pst.setString(2,"meet");
        pst.setDouble(3,96);
        int i=pst.executeUpdate();
        System.out.println((i>0)?"Inserted":"failde"); */

       String sql2="select * from student where id=?";
        PreparedStatement pst2=con.prepareStatement(sql2);
        pst2.setInt(1,5);
        ResultSet rs=pst2.executeQuery();
        while(rs.next()){
            System.out.println(rs.getString(2));
            System.out.println(rs.getDouble(3));
        }


    }
}