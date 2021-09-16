package newscreen

import newscreen.files.ProjectStructure
import newscreen.files.SourceRoot

interface SourceRootRepository {

    fun findFirstModuleSourceRoot(): SourceRoot?
}
class SourceRootRepositoryImpl(private val projectStructure: ProjectStructure) : SourceRootRepository {
    override fun findFirstModuleSourceRoot() =
        projectStructure.findSourceRoots().firstOrNull {
            !it.path.contains("build", true)
                    && !it.path.contains("test", true)
                    && !it.path.contains("res", true)
        }
}