package io.github.yamilmedina.gitsvn.mojo;

/**
 *
 * @author Y.Medina
 */
public enum RevisionControlSystem {

    GIT {
                @Override
                public String toString() {
                    return "git";
                }
            },
    SVN {
                @Override
                public String toString() {
                    return "svn";
                }

            };
}
