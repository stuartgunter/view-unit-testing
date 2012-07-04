<#import "/spring.ftl" as spring>
<span id="${item.id}"><@spring.message "key"/>: ${item.text}</span>
<span id="component">${component.someText}</span>
${component.doSomething()}
<#include "included.ftl"/>