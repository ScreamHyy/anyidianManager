$.extend($.fn.validatebox.defaults.rules, {
    minLength: {
        validator: function(value, param){
            return value.length >= param[0];
        },
        message: '输入的字符串至少需要 {0} 个字符.'
    },
    maxLength: {
        validator: function(value, param){
            return value.length <= param[0];
        },
        message: '输入的字符串至多只能 {0} 个字符.'
    },
    /*必须和某个字段相等*/
    equalTo: { 
    	validator: function (value, param) { 
    		return $(param[0]).val() == value; 
    	}, 
    	message: '两次输入的字段不匹配' 
    },
    digits: { 
    	validator: function (value) { 
    		return /^\d+$/.test(value); 
    	}, 
    	message: '请输入整数' 
    },
    phone: { 
    	validator: function (value) { 
    		return /^[0-9 \(\)]{7,30}$/.test(value); 
    	}, 
    	message: '请输入电话号码' 
    },
    mobile: { 
    	validator: function (value) { 
    		return /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/.test(value); 
    	}, 
    	message: '请输入手机号码' 
    }
});

function formatDate(date, format) {   
    if (!date) return;   
    if (!format) format = "yyyy-MM-dd";   
    switch(typeof date) {   
        case "string":   
            date = new Date(date.replace(/-/, "/"));   
            break;   
        case "number":   
            date = new Date(date);   
            break;   
    }    
    if (!date instanceof Date) return;   
    var dict = {   
        "yyyy": date.getFullYear(),   
        "M": date.getMonth() + 1,   
        "d": date.getDate(),   
        "H": date.getHours(),   
        "m": date.getMinutes(),   
        "s": date.getSeconds(),   
        "MM": ("" + (date.getMonth() + 101)).substr(1),   
        "dd": ("" + (date.getDate() + 100)).substr(1),   
        "HH": ("" + (date.getHours() + 100)).substr(1),   
        "mm": ("" + (date.getMinutes() + 100)).substr(1),   
        "ss": ("" + (date.getSeconds() + 100)).substr(1)   
    };       
    return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g, function() {   
        return dict[arguments[0]];   
    });                   
}   