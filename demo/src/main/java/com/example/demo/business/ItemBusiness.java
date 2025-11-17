package com.example.demo.business;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("itemBusiness")
public class ItemBusiness extends BaseBusiness  {
    
}
