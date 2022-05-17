package cn.px.sys.core.util;

import lombok.Data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Data
public class Jdbcresult {

    public PreparedStatement pt;
    public ResultSet rs;
}
