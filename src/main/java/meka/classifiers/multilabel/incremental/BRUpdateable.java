/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package meka.classifiers.multilabel.incremental;

import meka.classifiers.multilabel.BR;
import meka.classifiers.multilabel.IncrementalMultiLabelClassifier;
import meka.core.MLUtils;
import weka.classifiers.UpdateableClassifier;
import weka.classifiers.trees.HoeffdingTree;
import weka.core.Instance;

/**
 * BRUpdateable.java - Updateable BR.
 * Must be run with an UpdateableClassifier base classifier.
 * @see BR
 * @author 		Jesse Read
 * @version 	September, 2011
 */
public class BRUpdateable extends BR implements IncrementalMultiLabelClassifier {

	/** for serialization. */
  	private static final long serialVersionUID = 6705611077773512052L;

	@Override
	public String globalInfo() {
		return "Updateable BR\nMust be run with an Updateable base classifier.";
	}

	public BRUpdateable() {
		// default classifier for GUI
		this.m_Classifier = new HoeffdingTree();
	}

	@Override
	protected String defaultClassifierString() {
		// default classifier for CLI
		return "weka.classifiers.trees.HoeffdingTree";
	}

	@Override
	public void updateClassifier(Instance x) throws Exception {

		int L = x.classIndex();

		if(getDebug()) System.out.print("-: Updating "+L+" models");

		for(int j = 0; j < L; j++) {
			Instance x_j = (Instance)x.copy();
			x_j.setDataset(null);
			x_j = MLUtils.keepAttributesAt(x_j,new int[]{j},L);
			x_j.setDataset(m_InstancesTemplates[j]);
			((UpdateableClassifier)m_MultiClassifiers[j]).updateClassifier(x_j);
		}

		if(getDebug()) System.out.println(":- ");
	}

	public static void main(String args[]) {
		IncrementalEvaluation.runExperiment(new BRUpdateable(),args);
	}

}
