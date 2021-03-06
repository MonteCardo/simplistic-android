package br.com.montecardo.simplistic.data.source.room

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.content.Context
import br.com.montecardo.simplistic.data.Node
import br.com.montecardo.simplistic.data.source.Repository

class RoomRepository(context: Context) : Repository {

    private val roomDb = Room
        .databaseBuilder(context, AppDatabase::class.java, "simplistic")
        .allowMainThreadQueries()
        .build()

    private val nodeDao = roomDb.nodeDao()

    override fun getSubItems(nodeId: Long?): List<Node> {
        val nodes = if (nodeId != null)
            nodeDao.findNodesByParentId(nodeId)
        else
            nodeDao.findRootNodes()

        return nodes.map { it.toCore() }
    }

    override fun getNode(nodeId: Long?) =
        if (nodeId != null) nodeDao.findNodeById(nodeId)?.toCore() else null

    override fun saveNode(node: Node) = nodeDao.insertNode(node.toRoom())

    override fun removeNode(nodeId: Long) {
        nodeDao.deleteNode(nodeId)
    }

    @Database(entities = arrayOf(RoomNode::class), version = 1, exportSchema = false)
    abstract class AppDatabase : RoomDatabase() {
        abstract fun nodeDao(): NodeDao
    }

    @Dao
    interface NodeDao {
        @Insert(onConflict = REPLACE)
        fun insertNode(task: RoomNode)

        @Update(onConflict = REPLACE)
        fun updateNode(task: RoomNode)

        @Query("delete from node where id = :id")
        fun deleteNode(id: Long): Int

        @Query("select * from node where parent_id = :id")
        fun findNodesByParentId(id: Long): List<RoomNode>

        @Query("select * from node where parent_id is NULL")
        fun findRootNodes(): List<RoomNode>

        @Query("select * from node where id = :id")
        fun findNodeById(id: Long): RoomNode?
    }
}