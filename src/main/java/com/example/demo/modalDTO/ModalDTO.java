package com.example.demo.modalDTO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModalDTO extends BaseUntil{

	private Integer id;
	private String name;
	private Boolean status;
	private String client;
	@Override
	public String toString() {
		return "ModalDTO [id=" + id + ", name=" + name + ", status=" + status + ", client=" + client
				+ ", getCreateOn()=" + getCreateOn() + ", getUpdateOn()=" + getUpdateOn() + ", getCreateBy()="
				+ getCreateBy() + ", getUpdateBy()=" + getUpdateBy() + ", toString()=" + super.toString()
				+ ", getClass()=" + getClass() + "]";
	}	
}

/*
{
    "id":1,
    "name":"ara",
    "status":true,
    "client":"web",
    "createBy":"ram",
    "updateBy":"r"
}
*/