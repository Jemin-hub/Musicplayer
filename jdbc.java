import java.sql.Connection;
import java.sql.DriverManager;
class jdbc {
    public static void main(String args[]) throws Exception {
        String url="jdbc:mysql://localhost:3306/project";
        String user="root";
        String pass="";
        String driver="con.mysql.cj.jdbc.Driver";
        Connection con=DriverManager.getConnection(url,user,pass);
        if(con!=null){
            System.out.println("connection is successful");
        }else{
            System.out.println("not connected");
        }
      /*   String sql="insert into student values(1,'jemin',95),(2,'Abhi',94),(3,'Archan',93)";//rit 1
        String sql1="insert into student values(4,'Dwarka Na Dev.wav',66)";
        Statement st=con.createStatement();
        int i=st.executeUpdate(sql1);
        System.out.println((i>0)?"Inserted":"failde"); */
        //select 
       /* String q="select * from student where name='jemin'";
        Statement st2=con.createStatement();
        ResultSet rs=st2.executeQuery(q);
        while(rs.next()){
            System.out.println(rs.getInt(1));
            System.out.println(rs.getString(2));
            System.out.println(rs.getDouble(3));
        }*/
        
      }

        
    }

