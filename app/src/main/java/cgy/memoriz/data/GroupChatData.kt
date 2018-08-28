package cgy.memoriz.data

import java.io.Serializable

class GroupChatData : Serializable {
    var groupID: String? = null
    var groupName: String? = null

    constructor() {}

    constructor(groupID: String?, groupName: String?) {
        this.groupID = groupID
        this.groupName = groupName
    }
}

